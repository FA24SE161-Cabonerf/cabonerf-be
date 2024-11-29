package com.example.cabonerfbe.services.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.*;
import com.example.cabonerfbe.response.GetAllOrganizationResponse;
import com.example.cabonerfbe.response.InviteMemberResponse;
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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    public static final String LCA_STAFF = "LCA Staff";
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationConverter organizationConverter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserVerifyStatusRepository userVerifyStatusRepository;
    @Autowired
    private UserOrganizationRepository userOrganizationRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private UserOrganizationConverter uoConverter;
    @Autowired
    private SocketIOServer server;
    @Autowired
    private ContractConverter contractConverter;
    @Autowired
    private ContractService contractService;
    @Autowired
    private EmailVerificationTokenRepository evtRepository;
    @Autowired
    private RoleConverter roleConverter;

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

        if (!isPdfFile(contractFile)) {
            throw CustomExceptions.badRequest("Contract file is not .pdf");
        }

        Optional<Users> user = userRepository.findByEmail(request.getEmail());
        String password = PasswordGenerator.generateRandomPassword(8);
//        if (user.isPresent())
//        {
////            if (Objects.equals(user.get().getRole().getName(), "Organization Manager")) {
////                throw CustomExceptions.badRequest("Email already use with other organization");
////            }
//        } else {
//            Users newUser = new Users();
//            newUser.setEmail(request.getEmail());
//            newUser.setFullName(request.getName());
//            newUser.setUserVerifyStatus(userVerifyStatusRepository.findByName("Pending").get());
//            newUser.setRole(roleRepository.findByName("Organization Manager").get());
//            newUser.setPassword(passwordEncoder.encode(password));
//            user = Optional.of(userRepository.save(newUser));
//        }

        if(user.isEmpty()){
            Users newAccount = new Users();
            newAccount.setEmail(request.getEmail());
            newAccount.setFullName(request.getName());
            newAccount.setUserVerifyStatus(userVerifyStatusRepository.findByName("Pending").get());
            newAccount.setRole(roleRepository.findByName("Organization Manager").get());
            newAccount.setPassword(passwordEncoder.encode(password));
            user = Optional.of(userRepository.save(newAccount));
        }

        Organization o = new Organization();

        o = organizationRepository.save(o);

        UserOrganization uo = new UserOrganization();
        uo.setOrganization(o);
        uo.setUser(user.get());
        uo.setHasJoined(false);
        uo.setRole(roleRepository.findByName("Organization Manager").get());
        userOrganizationRepository.save(uo);

        //send-mail

        //storage contract
        String url = "";
        try {
            url = s3Service.uploadContract(contractFile);
        } catch (Exception ignored) {
        }

        Contract c = new Contract();
        c.setOrganization(o);
        c.setUrl(url);
        c = contractRepository.save(c);

        o.setName(request.getName());
        o.setContract(c);
        o.setLogo("");
        o = organizationRepository.save(o);
        return organizationConverter.modelToDto(o);
    }

    @Override
    public OrganizationDto updateOrganization(UUID organizationId, UpdateOrganizationRequest request) {
        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        o.setName(request.getName());
        return organizationConverter.modelToDto(organizationRepository.save(o));
    }

    @Override
    public OrganizationDto deleteOrganization(UUID organizationId) {
        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        contractService.deleteContract(o.getContract().getId());
        s3Service.deleteFile(o.getLogo());
        o.setStatus(false);
        return organizationConverter.modelToDto(organizationRepository.save(o));
    }

    @Override
    public LoginResponse confirm(VerifyCreateOrganizationRequest request) {
        Organization o = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        EmailVerificationToken _token = jwtService.checkToken(request.getToken());

        Users u = userRepository.findById(_token.getUsers().getId())
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        UserOrganization uo = userOrganizationRepository.findByUserAndOrganization(o.getId(), u.getId())
                .orElseThrow(() -> CustomExceptions.notFound("User doesn't belong to organization."));

        if (u.getRole().getName().equals("Verified")) {
            if(uo.isHasJoined()){
                throw CustomExceptions.badRequest("Account is already join organization.");
            }
        }
        u.setUserVerifyStatus(userVerifyStatusRepository.findByName("Verified").get());
        userRepository.save(u);

        uo.setHasJoined(true);
        userOrganizationRepository.save(uo);

        _token.setValid(false);
        evtRepository.save(_token);

        var access_token = jwtService.generateToken(u);
        var refresh_token = jwtService.generateRefreshToken(u);

        authenticationService.saveRefreshToken(refresh_token, u);

        return LoginResponse.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .user(userConverter.fromUserToUserDto(u))
                .build();
    }

    @Override
    public InviteMemberResponse invite(UUID userId, InviteUserToOrganizationRequest request) {

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

//        UserOrganization organizationManager = userOrganizationRepository.findByUserAndOrganization(request.getOrganizationId(), userId)
//                .orElseThrow(() -> CustomExceptions.unauthorized("You do not belong to this organization."));
//
//        if(!Objects.equals(organizationManager.getRole().getName(), "Organization Manager")){
//            throw CustomExceptions.unauthorized("Your role not support this action");
//        }

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
        if (!checkInvite.isEmpty()) {
            Set<UUID> userHaveInvite = checkInvite.stream()
                    .map(UserOrganization::getUser)
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
                    user.setRole(roleRepository.findByName(LCA_STAFF).orElseThrow());
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
                    uo.setRole(roleRepository.findByName(LCA_STAFF).orElseThrow());
                    uo.setHasJoined(false);
                    return uo;
                }).collect(Collectors.toList());

        userOrganizations = userOrganizationRepository.saveAll(userOrganizations);

        for (UserOrganization x : userOrganizations) {
            server.getRoomOperations(x.getUser().getId().toString()).sendEvent("newInvite", uoConverter.enityToDto(x));
        }

        List<InviteUserOrganizationDto> members = userOrganizations.stream()
                .map(userOrg -> {
                    InviteUserOrganizationDto memberDto = new InviteUserOrganizationDto();
                    memberDto.setId(userOrg.getId());
                    memberDto.setUser(userConverter.forInvite(userOrg.getUser()));
                    memberDto.setRole(roleConverter.fromRoleToRoleDto(userOrg.getRole()));
                    memberDto.setHasJoined(userOrg.isHasJoined());
                    return memberDto;
                })
                .collect(Collectors.toList());

        return InviteMemberResponse.builder()
                .newMembers(members)
                .newAccountIds(newUsers.stream().map(Users::getId).collect(Collectors.toList()))
                .build();

    }

    @Override
    public UserOrganizationDto acceptDenyInvite(UUID userId, AcceptInviteRequest request, String action) {

        UserOrganization uo = userOrganizationRepository.findById(request.getUserOrganizationId())
                .orElseThrow(() -> CustomExceptions.notFound("User doesn't have invitation to organization."));

        if (uo.isHasJoined()) {
            throw CustomExceptions.badRequest("User already joined organization.");
        }
        switch (action) {
            case "Accept":
                uo.setHasJoined(true);
                userOrganizationRepository.save(uo);
                break;
            case "Deny":
                userOrganizationRepository.delete(uo);
                break;
            default:
                break;
        }
        userOrganizationRepository.save(uo);

        return uoConverter.enityToDto(uo);
    }

    @Override
    public List<InviteUserOrganizationDto> getMemberInOrganization(UUID organizationId) {

        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        List<UserOrganization> member = userOrganizationRepository.findByOrganization(organizationId);

        List<InviteUserOrganizationDto> data = new ArrayList<>();
        for (UserOrganization x : member){
            InviteUserOrganizationDto memberDto = new InviteUserOrganizationDto();
            memberDto.setId(x.getId());
            memberDto.setUser(userConverter.forInvite(x.getUser()));
            memberDto.setRole(roleConverter.fromRoleToRoleDto(x.getRole()));
            memberDto.setHasJoined(x.isHasJoined());
            data.add(memberDto);
        }
        return data;
    }

    @Override
    public List<UserOrganizationDto> getListInviteByUser(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound("User not exist"));

        List<UserOrganization> history = userOrganizationRepository.getByUser(userId);

        return history.stream().map(uoConverter::enityToDto).collect(Collectors.toList());
    }

    @Override
    public OrganizationDto uploadLogo(UUID organizationId, MultipartFile logo) {
        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        o.setLogo(s3Service.uploadImage(logo));
        return organizationConverter.modelToDto(o);
    }

    @Override
    public List<GetOrganizationByUserDto> getByUser(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        List<UserOrganization> uo = userOrganizationRepository.getByUser(userId);

        return uo.stream()
                .map(UserOrganization::getOrganization)
                .map(organization -> {
                    GetOrganizationByUserDto dto = organizationConverter.modelToUser(organization);
                    // Kiểm tra nếu contract bị null thì set isDefault = true
                    dto.setDefault(organization.getContract() == null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationDto getOrganizationById(UUID organizationId) {
        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));
        return organizationConverter.modelToDto(o);
    }

    @Override
    public InviteUserOrganizationDto removeMember(UUID userId, UUID userOrganizationId) {
//        UserOrganization organizationManager = userOrganizationRepository.findByUserAndOrganization(request.getOrganizationId(), userId)
//                .orElseThrow(() -> CustomExceptions.unauthorized("You do not belong to this organization."));
//
//        if(!Objects.equals(organizationManager.getRole().getName(), "Organization Manager")){
//            throw CustomExceptions.unauthorized("Your role not support this action");
//        }
        UserOrganization uo = userOrganizationRepository.findById(userOrganizationId)
                .orElseThrow(() -> CustomExceptions.notFound("Member do not belong to this organization"));

        uo.setStatus(false);
        return uoConverter.modelToDto(uo);
    }

    private boolean isPdfFile(MultipartFile file) {
        // Cách 1: Kiểm tra theo Content Type
        if (file.getContentType() != null && file.getContentType().equals("application/pdf")) {
            return true;
        }

        // Cách 2: Kiểm tra theo đuôi file
        String filename = file.getOriginalFilename();
        if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
            return true;
        }

        // Cách 3: Kiểm tra Magic Number của PDF file
        try {
            byte[] bytes = file.getBytes();
            if (bytes.length > 4 &&
                    bytes[0] == 0x25 && // %
                    bytes[1] == 0x50 && // P
                    bytes[2] == 0x44 && // D
                    bytes[3] == 0x46 && // F
                    bytes[4] == 0x2D) { // -
                return true;
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }

}
