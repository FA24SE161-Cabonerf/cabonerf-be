package com.example.cabonerfbe.services.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.cabonerfbe.converter.OrganizationConverter;
import com.example.cabonerfbe.converter.UserConverter;
import com.example.cabonerfbe.converter.UserOrganizationConverter;
import com.example.cabonerfbe.dto.InviteUserOrganizationDto;
import com.example.cabonerfbe.dto.OrganizationDto;
import com.example.cabonerfbe.dto.UserOrganizationDto;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.GetAllOrganizationResponse;
import com.example.cabonerfbe.response.GetInviteListResponse;
import com.example.cabonerfbe.response.LoginResponse;
import com.example.cabonerfbe.services.*;
import com.example.cabonerfbe.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationConverter organizationConverter;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserVerifyStatusRepository userVerifyStatusRepository;
    @Autowired
    private UserOrganizationRepository userOrganizationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private UserOrganizationConverter uoConverter;
    @Autowired
    private SocketIOServer server;

    @Override
    public GetAllOrganizationResponse getAll(int pageCurrent, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);


        Page<Organization> data = keyword == null ? organizationRepository.findAll(pageable) : organizationRepository.findAllByKeyword(keyword, pageable);

        int totalPage = data.getTotalPages();

        if (pageCurrent > totalPage) {
            return GetAllOrganizationResponse.builder()
                    .totalPage(0)
                    .pageCurrent(1)
                    .pageSize(0)
                    .list(Collections.emptyList())
                    .build();
        }

        return GetAllOrganizationResponse.builder()
                .totalPage(totalPage)
                .pageCurrent(pageCurrent)
                .pageSize(pageSize)
                .list(data.stream().map(organizationConverter::modelToDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public OrganizationDto createOrganization(CreateOrganizationRequest request, MultipartFile contractFile) {

        Optional<Users> user = userRepository.findByEmail(request.getEmail());
        String password = PasswordGenerator.generateRandomPassword(8);
        if (user.isPresent()) {
            if (Objects.equals(user.get().getRole().getName(), "Organization Manager")) {
                throw CustomExceptions.badRequest("Email already use with other organization");
            }

            user.get().setRole(roleRepository.findByName("Organization Manager").get());
            userRepository.save(user.get());
        } else {
            Users newUser = new Users();
            newUser.setEmail(request.getEmail());
            newUser.setFullName(request.getName());
            newUser.setUserVerifyStatus(userVerifyStatusRepository.findByName("Pending").get());
            newUser.setRole(roleRepository.findByName("Organization Manager").get());
            newUser.setPassword(passwordEncoder.encode(password));
            user = Optional.of(userRepository.save(newUser));
        }

        Organization o = new Organization();

        o = organizationRepository.save(o);

        UserOrganization uo = new UserOrganization();
        uo.setOrganization(o);
        uo.setUser(user.get());
        uo.setRole(roleRepository.findByName("Organization Manager").get());
        userOrganizationRepository.save(uo);

        //send-mail

        //storage contract
        String url = "";
        try {
            String key = "contract/" + contractFile.getOriginalFilename();
            byte[] file = contractFile.getBytes();

            url = s3Service.updateFile(key, file);
        } catch (Exception ignored) {
        }

        Contract c = new Contract();
        c.setOrganization(o);
        c.setUrl(url);
        c = contractRepository.save(c);

        o.setName(request.getName());
        o.setContract(c);
        o = organizationRepository.save(o);
        return organizationConverter.modelToDto(o);
    }

    @Override
    public OrganizationDto updateOrganization(UUID organizationId, UpdateOrganizationRequest request) {
        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound("Organization not exist"));

        o.setName(request.getName());
        return organizationConverter.modelToDto(organizationRepository.save(o));
    }

    @Override
    public OrganizationDto deleteOrganization(UUID organizationId) {
        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound("Organization not exist"));
        o.setStatus(false);
        return organizationConverter.modelToDto(organizationRepository.save(o));
    }

    @Override
    public LoginResponse confirm(UUID userId, VerifyCreateOrganizationRequest request) {
        Organization o = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> CustomExceptions.notFound("Organization not exist"));

        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound("User not exist"));

        u.setUserVerifyStatus(userVerifyStatusRepository.findByName("Verified").get());
        userRepository.save(u);

        var access_token = jwtService.generateToken(u);
        var refresh_token = jwtService.generateRefreshToken(u);

        authenticationService.saveRefreshToken(refresh_token, u);

        UserOrganization uo = userOrganizationRepository.findByUserAndOrganization(o.getId(), u.getId())
                .orElseThrow(() -> CustomExceptions.notFound("User not belonging to organization"));

        Workspace w = new Workspace();
        w.setName(o.getName());
        w.setOwner(u);
        w.setOrganization(o);

        workspaceRepository.save(w);

        return LoginResponse.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .user(userConverter.fromUserToUserDto(u))
                .build();
    }

    @Override
    public List<InviteUserOrganizationDto> invite(InviteUserToOrganizationRequest request) {

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> CustomExceptions.notFound("Organization not exists"));

        List<Users> existingUsers = userRepository.findAllByEmail(request.getUserEmail());


        Set<String> existingEmails = existingUsers.stream()
                .map(Users::getEmail)
                .collect(Collectors.toSet());

        // Find new emails not in database
        List<String> newEmails = request.getUserEmail().stream()
                .filter(email -> !existingEmails.contains(email))
                .collect(Collectors.toList());

        List<UUID> userIds = existingUsers.stream().map(Users::getId).collect(Collectors.toList());

        List<UserOrganization> checkInvite = userOrganizationRepository.findInvite(userIds, organization.getId());
        if(!checkInvite.isEmpty()){
            Set<UUID> userHaveInvite = checkInvite.stream()
                    .map(UserOrganization :: getUser)
                    .map(Users::getId)
                    .collect(Collectors.toSet());
            existingUsers = existingUsers.stream()
                    .filter(user -> !userHaveInvite.contains(user.getId())) // Lọc các user không có trong userHaveInvite
                    .collect(Collectors.toList());
        }
        // Create and save new users
        List<Users> newUsers = newEmails.stream()
                .map(email -> {
                    Users user = new Users();
                    user.setEmail(email);
                    user.setFullName(email.split("@")[0]);
                    user.setUserVerifyStatus(userVerifyStatusRepository.findByName("Pending").orElseThrow());
                    user.setRole(roleRepository.findByName("LCA Staff").orElseThrow());
                    user.setPassword(passwordEncoder.encode(PasswordGenerator.generateRandomPassword(8)));
                    return user;
                }).collect(Collectors.toList());

        newUsers = userRepository.saveAll(newUsers);

        // Combine existing and new users
        List<Users> allUsers = new ArrayList<>(existingUsers);
        allUsers.addAll(newUsers);

        // Create UserOrganization entities
        List<UserOrganization> userOrganizations = allUsers.stream()
                .map(user -> {
                    UserOrganization uo = new UserOrganization();
                    uo.setOrganization(organization);
                    uo.setUser(user);
                    uo.setRole(roleRepository.findByName("LCA Staff").orElseThrow());
                    uo.setHasJoined(false);
                    return uo;
                }).collect(Collectors.toList());

        userOrganizations = userOrganizationRepository.saveAll(userOrganizations);

        for(UserOrganization x : userOrganizations){
            server.getRoomOperations(x.getUser().getId().toString()).sendEvent("invite",uoConverter.enityToDto(x));
        }
        // TODO: Send emails to users to accept join
        return userOrganizations.stream()
                .map(uoConverter::modelToDto)
                .collect(Collectors.toList());

    }

    @Override
    public UserOrganizationDto acceptDenyInvite(UUID userId, AcceptInviteRequest request,String action) {
        Organization o = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> CustomExceptions.notFound("Organization not exist"));
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound("User not exist"));

        if (!u.isStatus()){
            throw CustomExceptions.unauthorized("User is ban");
        }

        UserOrganization uo = userOrganizationRepository.findByUserAndOrganization(request.getOrganizationId(), userId)
                .orElseThrow(() -> CustomExceptions.notFound("User not have invite to organization"));

        if(uo.isHasJoined()){
            throw CustomExceptions.badRequest("User already join organization");
        }
        switch (action){
            case "Accept":
                uo.setHasJoined(true);
                break;
            case "Deny":
                uo.setStatus(false);
                break;
            default: break;
        }
        userOrganizationRepository.save(uo);

        return uoConverter.enityToDto(uo);
    }

}
