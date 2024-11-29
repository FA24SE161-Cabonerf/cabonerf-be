package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.GetOrganizationByUserDto;
import com.example.cabonerfbe.dto.InviteUserOrganizationDto;
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
import com.example.cabonerfbe.response.UploadOrgLogoResponse;
import com.example.cabonerfbe.services.*;
import com.example.cabonerfbe.util.FileUtil;
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
    private ContractConverter contractConverter;
    @Autowired
    private ContractService contractService;
    @Autowired
    private EmailVerificationTokenRepository evtRepository;
    @Autowired
    private RoleConverter roleConverter;
    @Autowired
    private FileUtil fileUtil;

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
    public OrganizationDto createOrganization(CreateOrganizationRequest request, MultipartFile contractFile, MultipartFile logo) {

        if (!fileUtil.isPdfFile(contractFile)) {
            throw CustomExceptions.badRequest(MessageConstants.INVALID_PDF);
        }

        if (!fileUtil.isImageFile(logo)) {
            throw CustomExceptions.badRequest(MessageConstants.INVALID_IMAGE);
        }

        String password = PasswordGenerator.generateRandomPassword(8);

        Role organizationManager = roleRepository.findByName(Constants.ORGANIZATION_MANAGER).orElseGet(
                () -> roleRepository.save(new Role(Constants.ORGANIZATION_MANAGER))
        );

        UserVerifyStatus pendingStatus = userVerifyStatusRepository.findByName(Constants.VERIFY_STATUS_PENDING).orElseGet(
                () -> userVerifyStatusRepository.save(new UserVerifyStatus(Constants.VERIFY_STATUS_PENDING, Constants.VERIFY_STATUS_PENDING))
        );

        Users user = userRepository.findByEmail(request.getEmail()).orElseGet(
                () -> {
                    Users newAccount = new Users();
                    newAccount.setEmail(request.getEmail());
                    newAccount.setFullName(request.getName());
                    newAccount.setUserVerifyStatus(pendingStatus);
                    newAccount.setRole(organizationManager);
                    newAccount.setPassword(passwordEncoder.encode(password));
                    return userRepository.save(newAccount);
                }
        );

        String logoUrl = "";
        String contractUrl = "";

        try {
            contractUrl = s3Service.uploadContract(contractFile);
        } catch (Exception ignored) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_UPLOAD_CONTRACT);
        }

        try {
            logoUrl = s3Service.uploadImage(logo);
        } catch (Exception ignored) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_UPLOAD_IMAGE);
        }

        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setLogo(logoUrl);

        organization = organizationRepository.save(organization);

        UserOrganization userOrganization = new UserOrganization();
        userOrganization.setOrganization(organization);
        userOrganization.setUser(user);
        userOrganization.setHasJoined(false);
        userOrganization.setRole(organizationManager);
        userOrganizationRepository.save(userOrganization);

        // todo: send-mail

        Contract contract = new Contract();
        contract.setOrganization(organization);
        contract.setUrl(contractUrl);

        organization.setContract(contract);
        organization = organizationRepository.save(organization);
        return organizationConverter.modelToDto(organization);
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
//        s3Service.deleteFile(o.getLogo());
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
            if (uo.isHasJoined()) {
                throw CustomExceptions.badRequest("Account has already joined organization.");
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
                    .filter(user -> !userHaveInvite.contains(user.getId()))
                    .collect(Collectors.toList());
        }
        // Create and save new users
        List<Users> newUsers = newEmails.stream()
                .map(email -> {
                    Users user = new Users();
                    user.setEmail(email);
                    user.setFullName(email.split("@")[0]);
                    user.setUserVerifyStatus(userVerifyStatusRepository.findByName("Pending").orElseThrow());
                    user.setRole(roleRepository.findByName(Constants.LCA_STAFF).orElseThrow());
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
                    uo.setRole(roleRepository.findByName(Constants.LCA_STAFF).orElseThrow());
                    uo.setHasJoined(false);
                    return uo;
                }).collect(Collectors.toList());

        userOrganizations = userOrganizationRepository.saveAll(userOrganizations);


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
    public LoginResponse acceptInvite(UUID userId, UUID organizationId, String token) {

        EmailVerificationToken _token = jwtService.checkToken(token);

        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        if(!u.isStatus()){
            throw CustomExceptions.unauthorized("Account is banned");
        }

        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));


        UserOrganization uo = userOrganizationRepository.findByUserAndOrganization(organizationId,userId)
                .orElseThrow(() -> CustomExceptions.unauthorized(MessageConstants.USER_NOT_BELONG_TO_ORGANIZATION));

        if (uo.isHasJoined()) {
            throw CustomExceptions.badRequest("User already joined organization.");
        }
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
    public List<InviteUserOrganizationDto> getMemberInOrganization(UUID organizationId) {

        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        List<UserOrganization> member = userOrganizationRepository.findByOrganization(organizationId);

        List<InviteUserOrganizationDto> data = new ArrayList<>();
        for (UserOrganization x : member) {
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
    public UploadOrgLogoResponse uploadLogo(UUID organizationId, MultipartFile logo) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        String logoUrl;
        try {
            logoUrl = s3Service.uploadImage(logo);
        } catch (Exception e) {
            throw CustomExceptions.badRequest(MessageConstants.FAILED_TO_UPLOAD_IMAGE);
        }

        return new UploadOrgLogoResponse(organization.getId(), logoUrl);
    }

    @Override
    public List<GetOrganizationByUserDto> getByUser(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        List<UserOrganization> uo = userOrganizationRepository.getByUser(userId);

        // Stream processing with role check
        return uo.stream()
                .map(UserOrganization::getOrganization)
                .map(organization -> {
                    GetOrganizationByUserDto dto = organizationConverter.modelToUser(organization);

                    boolean isOrganizationManager = uo.stream()
                            .filter(us -> us.getOrganization().equals(organization))
                            .anyMatch(us -> Constants.ORGANIZATION_MANAGER.equalsIgnoreCase(us.getRole().getName()));

                    dto.setDefault(isOrganizationManager && organization.getContract() == null);
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


}
