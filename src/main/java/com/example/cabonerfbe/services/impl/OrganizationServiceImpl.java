package com.example.cabonerfbe.services.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.InviteUserOrganizationDto;
import com.example.cabonerfbe.dto.MemberOrganizationDto;
import com.example.cabonerfbe.dto.OrganizationDto;
import com.example.cabonerfbe.dto.UserOrganizationDto;
import com.example.cabonerfbe.enums.Constants;
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
            throw CustomExceptions.badRequest(MessageConstants.INVALID_PDF);
        }

        Optional<Users> user = userRepository.findByEmail(request.getEmail());
        Role organizationManager = roleRepository.findByName(Constants.ORGANIZATION_MANAGER).orElseGet(
                () -> new Role(Constants.ORGANIZATION_MANAGER)
        );
        if (user.isPresent()) {
            if (Constants.ORGANIZATION_MANAGER.equals(user.get().getRole().getName())) {
                throw CustomExceptions.badRequest(MessageConstants.EMAIL_IS_USED_FOR_OTHER_ORGANIZATION);
            }
            user.get().setRole(organizationManager);
            userRepository.save(user.get());
        } else {
            Users newUser = new Users();
            newUser.setEmail(request.getEmail());
            newUser.setFullName(request.getName());
            newUser.setUserVerifyStatus(userVerifyStatusRepository.findByName(Constants.VERIFY_STATUS_PENDING).orElseGet(
                    () -> new UserVerifyStatus(Constants.VERIFY_STATUS_PENDING, Constants.VERIFY_STATUS_PENDING)));
            newUser.setRole(organizationManager);
            newUser.setPassword(passwordEncoder.encode(PasswordGenerator.generateRandomPassword(8)));
            user = Optional.of(userRepository.save(newUser));
        }

        //storage contract
        String url = "";
        try {
            url = s3Service.uploadContract(contractFile);
        } catch (Exception ignored) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_UPLOAD_CONTRACT);
        }

        Organization organization = new Organization();
        organization.setName(request.getName());

        Contract contract = new Contract();
        contract.setOrganization(organization);
        contract.setUrl(url);

        organization.setContract(contract);

        // cascade.all -> auto save contract
        organization = organizationRepository.save(organization);

        UserOrganization userOrganization = new UserOrganization();
        userOrganization.setOrganization(organization);
        userOrganization.setUser(user.get());
        userOrganization.setRole(organizationManager);
        userOrganizationRepository.save(userOrganization);

        return organizationConverter.modelToDto(organization);
    }

    @Override
    public OrganizationDto updateOrganization(UUID organizationId, UpdateOrganizationRequest request) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.badRequest(MessageConstants.NO_ORGANIZATION_FOUND));

        organization.setName(request.getName());
        return organizationConverter.modelToDto(organizationRepository.save(organization));
    }

    @Override
    public OrganizationDto deleteOrganization(UUID organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        contractService.deleteContract(organization.getContract().getId());
        organization.setStatus(Constants.STATUS_FALSE);
        return organizationConverter.modelToDto(organizationRepository.save(organization));
    }

    @Override
    public LoginResponse confirm(VerifyCreateOrganizationRequest request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        EmailVerificationToken _token = jwtService.checkToken(request.getToken());

        Users u = userRepository.findById(_token.getUsers().getId())
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        if (!u.getRole().getName().equals("Verified")) {
            throw CustomExceptions.badRequest("Account is already verified.");
        }

        u.setUserVerifyStatus(userVerifyStatusRepository.findByName("Verified").get());
        userRepository.save(u);

        _token.setValid(false);
        evtRepository.save(_token);

        var access_token = jwtService.generateToken(u);
        var refresh_token = jwtService.generateRefreshToken(u);

        authenticationService.saveRefreshToken(refresh_token, u);

        UserOrganization uo = userOrganizationRepository.findByUserAndOrganization(organization.getId(), u.getId())
                .orElseThrow(() -> CustomExceptions.notFound("User doesn't belong to organization."));

        Workspace personWorkspace = new Workspace();
        personWorkspace.setName("My Workspace");
        personWorkspace.setOwner(u);
        personWorkspace.setOrganization(null);

        Workspace w = new Workspace();
        w.setName(organization.getName());
        w.setOwner(u);
        w.setOrganization(organization);

        workspaceRepository.saveAll(List.of(personWorkspace, w));

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

        // TODO: Send emails to users to accept join
        return InviteMemberResponse.builder()
                .members(members)
                .newAccountIds(newUsers.stream().map(Users::getId).collect(Collectors.toList()))
                .build();

    }

    @Override
    public UserOrganizationDto acceptDenyInvite(UUID userId, AcceptInviteRequest request, String action) {

        UserOrganization uo = userOrganizationRepository.findById(request.getId())
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
    public List<MemberOrganizationDto> getMemberInOrganization(UUID userId) {
        return userOrganizationRepository.findByUser(userId).stream()
                .map(userOrg -> userOrg.getOrganization().getId())
                .map(orgId -> {
                    MemberOrganizationDto dto = new MemberOrganizationDto();
                    dto.setId(orgId);

                    List<InviteUserOrganizationDto> members = userOrganizationRepository
                            .findByOrganization(orgId).stream()
                            .map(userOrg -> {
                                InviteUserOrganizationDto memberDto = new InviteUserOrganizationDto();
                                memberDto.setId(userOrg.getId());
                                memberDto.setUser(userConverter.forInvite(userOrg.getUser()));
                                memberDto.setRole(roleConverter.fromRoleToRoleDto(userOrg.getRole()));
                                memberDto.setHasJoined(userOrg.isHasJoined());
                                return memberDto;
                            })
                            .collect(Collectors.toList());

                    dto.setMembers(members);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserOrganizationDto> getListInviteByUser(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound("User not exist"));

        List<UserOrganization> history = userOrganizationRepository.getByUser(userId);

        return history.stream().map(uoConverter::enityToDto).collect(Collectors.toList());
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
