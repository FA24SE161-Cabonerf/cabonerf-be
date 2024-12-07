package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.CreateOrganizationDto;
import com.example.cabonerfbe.dto.GetOrganizationByUserDto;
import com.example.cabonerfbe.dto.InviteUserOrganizationDto;
import com.example.cabonerfbe.dto.OrganizationDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateOrganizationRequest;
import com.example.cabonerfbe.request.InviteUserToOrganizationRequest;
import com.example.cabonerfbe.request.UpdateOrganizationRequest;
import com.example.cabonerfbe.response.GetAllOrganizationResponse;
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
    private InviteOrganizationTokenRepository iotRepository;
    @Autowired
    private RoleConverter roleConverter;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private IndustryCodeRepository icRepository;
    @Autowired
    private OrganizationIndustryCodeRepository oicRepository;
    @Autowired
    private OrganizationIndustryCodeConverter oicConverter;
    @Autowired
    private IndustryCodeConverter icConverter;

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
    public CreateOrganizationDto createOrganization(CreateOrganizationRequest request, MultipartFile contractFile, MultipartFile logo) {

        if (!fileUtil.isPdfFile(contractFile)) {
            throw CustomExceptions.badRequest(MessageConstants.INVALID_PDF);
        }

        if (!fileUtil.isImageFile(logo)) {
            throw CustomExceptions.badRequest(MessageConstants.INVALID_IMAGE);
        }

        String password = PasswordGenerator.generateRandomPassword(8);
        CreateOrganizationDto dto = new CreateOrganizationDto();
        Role organizationManager = roleRepository.findByName(Constants.ORGANIZATION_MANAGER).orElseGet(
                () -> roleRepository.save(new Role(Constants.ORGANIZATION_MANAGER))
        );

        UserVerifyStatus pendingStatus = userVerifyStatusRepository.findByName(Constants.VERIFY_STATUS_PENDING).orElseGet(
                () -> userVerifyStatusRepository.save(new UserVerifyStatus(Constants.VERIFY_STATUS_PENDING, Constants.VERIFY_STATUS_PENDING))
        );

        List<IndustryCode> industryCodes = icRepository.findAllByIds(request.getIndustryCodeIds());

        if (industryCodes == null || industryCodes.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.NO_INDUSTRY_CODE_FOUND);
        }

        if (industryCodes.size() < request.getIndustryCodeIds().size()) {
            List<UUID> existingIndustryCodeIds = industryCodes.stream()
                    .map(IndustryCode::getId)
                    .toList();

            List<UUID> missingIndustryCodeIds = request.getIndustryCodeIds().stream()
                    .filter(codeId -> !existingIndustryCodeIds.contains(codeId))
                    .toList();

            if (!missingIndustryCodeIds.isEmpty()) {
                throw CustomExceptions.notFound(
                        String.format("Industry codes not found: %s", missingIndustryCodeIds)
                );
            }
        }

        CreateOrganizationDto finalDto = dto;
        Users user = userRepository.findByEmail(request.getEmail()).orElseGet(
                () -> {
                    Users newAccount = new Users();
                    newAccount.setEmail(request.getEmail());
                    newAccount.setFullName(request.getName());
                    newAccount.setUserVerifyStatus(pendingStatus);
                    newAccount.setRole(organizationManager);
                    newAccount.setPassword(passwordEncoder.encode(password));
                    newAccount.setProfilePictureUrl(Constants.DEFAULT_USER_IMAGE);
                    newAccount = userRepository.save(newAccount);
                    finalDto.setNewUserId(newAccount.getId());

                    Organization o = new Organization();
                    o.setName(Constants.DEFAULT_ORGANIZATION);
                    o.setContract(null);
                    o.setLogo(Constants.DEFAULT_USER_IMAGE);
                    o = organizationRepository.save(o);

                    UserOrganization uo = new UserOrganization();
                    uo.setUser(newAccount);
                    uo.setOrganization(o);
                    uo.setHasJoined(true);
                    uo.setRole(organizationManager);
                    userOrganizationRepository.save(uo);

                    return newAccount;
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
        organization.setDescription(request.getDescription());
        organization.setTaxCode(request.getTaxCode());


        organization = organizationRepository.save(organization);

        UserOrganization userOrganization = new UserOrganization();
        userOrganization.setOrganization(organization);
        userOrganization.setUser(user);
        userOrganization.setHasJoined(false);
        userOrganization.setRole(organizationManager);
        userOrganizationRepository.save(userOrganization);


        Contract contract = new Contract();
        contract.setOrganization(organization);
        contract.setUrl(contractUrl);

        organization.setContract(contract);
        organization = organizationRepository.save(organization);

        List<OrganizationIndustryCode> oicList = new ArrayList<>();

        for(IndustryCode x : industryCodes){
            OrganizationIndustryCode oic = new OrganizationIndustryCode();
            oic.setOrganization(organization);
            oic.setIndustryCode(x);
            oicList.add(oic);
        }

        oicList = oicRepository.saveAll(oicList);

        dto = organizationConverter.modelToCreateDto(organization);
        dto.setIndustryCodes(industryCodes.stream().map(icConverter::modelToDto).toList());
        if (finalDto.getNewUserId() != null) {
            dto.setNewUserId(finalDto.getNewUserId());
        }
        return dto;
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

//    @Override
//    public LoginResponse confirm(VerifyCreateOrganizationRequest request) {
//        Organization o = organizationRepository.findByIdWhenCreate(request.getOrganizationId())
//                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));
//
//        EmailVerificationToken _token = jwtService.checkToken(request.getToken());
//
//        Users u = userRepository.findById(_token.getUsers().getId())
//                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));
//
//        UserOrganization uo = userOrganizationRepository.findByUserAndOrganization(o.getId(), u.getId())
//                .orElseThrow(() -> CustomExceptions.notFound("User doesn't belong to organization."));
//
//        if (u.getRole().getName().equals("Verified")) {
//            if (uo.isHasJoined()) {
//                throw CustomExceptions.badRequest("Account has already joined organization.");
//            }
//        }
//        u.setUserVerifyStatus(userVerifyStatusRepository.findByName("Verified").get());
//        userRepository.save(u);
//
//        o.setStatus(false);
//        organizationRepository.save(o);
//
//        uo.setHasJoined(true);
//        userOrganizationRepository.save(uo);
//
//        _token.setValid(false);
//        evtRepository.save(_token);
//
//        var access_token = jwtService.generateToken(u);
//        var refresh_token = jwtService.generateRefreshToken(u);
//
//        authenticationService.saveRefreshToken(refresh_token, u);
//
//        return LoginResponse.builder()
//                .access_token(access_token)
//                .refresh_token(refresh_token)
//                .user(userConverter.fromUserToUserDto(u))
//                .build();
//    }

    @Override
    public List<InviteUserOrganizationDto> invite(UUID userId, InviteUserToOrganizationRequest request) {

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

//        UserOrganization organizationManager = userOrganizationRepository.findByUserAndOrganization(request.getOrganizationId(), userId)
//                .orElseThrow(() -> CustomExceptions.unauthorized("You do not belong to this organization."));
//
//        if(!Objects.equals(organizationManager.getRole().getName(), "Organization Manager")){
//            throw CustomExceptions.unauthorized("Your role not support this action");
//        }

        List<Users> existingUsers = userRepository.findAllByEmail(request.getUserIds());

        if (existingUsers == null || existingUsers.isEmpty()) {
            throw CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND);
        }

        if (existingUsers.size() < request.getUserIds().size()) {
            List<UUID> existingUserIds = existingUsers.stream()
                    .map(Users::getId)
                    .toList();

            List<UUID> missingUserIds = request.getUserIds().stream()
                    .filter(uId -> !existingUserIds.contains(uId))
                    .toList();

            if (!missingUserIds.isEmpty()) {
                throw CustomExceptions.notFound(
                        String.format("Users not found: %s", missingUserIds)
                );
            }
        }

        List<UUID> userIds = existingUsers.stream().map(Users::getId).collect(Collectors.toList());

        List<UserOrganization> checkInvite = userOrganizationRepository.findInvite(userIds, organization.getId());
        if (!checkInvite.isEmpty()) {
            Set<UUID> userHaveInvite = checkInvite.stream()
                    .map(UserOrganization::getUser)
                    .map(Users::getId)
                    .collect(Collectors.toSet());
            existingUsers = existingUsers.stream()
                    .filter(user -> !userHaveInvite.contains(user.getId()))
                    .toList();
        }

        List<Users> allUsers = new ArrayList<>(existingUsers);

        // Create UserOrganization entities
        List<UserOrganization> userOrganizations = allUsers.stream()
                .map(user -> {
                    UserOrganization uo = new UserOrganization();
                    uo.setOrganization(organization);
                    uo.setUser(user);
                    uo.setRole(roleRepository.findByName(Constants.LCA_STAFF).orElseThrow());
                    uo.setHasJoined(true);
                    return uo;
                }).collect(Collectors.toList());

        userOrganizations = userOrganizationRepository.saveAll(userOrganizations);


        return userOrganizations.stream()
                .map(userOrg -> {
                    InviteUserOrganizationDto memberDto = new InviteUserOrganizationDto();
                    memberDto.setId(userOrg.getId());
                    memberDto.setUser(userConverter.forInvite(userOrg.getUser()));
                    memberDto.setRole(roleConverter.fromRoleToRoleDto(userOrg.getRole()));
                    memberDto.setHasJoined(userOrg.isHasJoined());
                    return memberDto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public LoginResponse acceptInvite(UUID userOrganizationId, String token) {

        InviteOrganizationToken _token = jwtService.checkInviteToken(token);
        UUID userId = _token.getUsers().getId();
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        if (!u.isStatus()) {
            throw CustomExceptions.unauthorized("Account is banned");
        }

        UserOrganization uo = userOrganizationRepository.findById(userOrganizationId)
                .orElseThrow(() -> CustomExceptions.unauthorized(MessageConstants.USER_NOT_HAVE_INVITE_ORGANIZATION));

        if (uo.isHasJoined()) {
            throw CustomExceptions.badRequest("User already joined organization.");
        }
        uo.setHasJoined(true);
        userOrganizationRepository.save(uo);

        _token.setValid(false);
        iotRepository.save(_token);

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
                .sorted((dto1, dto2) -> Boolean.compare(dto2.isDefault(), dto1.isDefault())) // Sort by default in descending order
                .collect(Collectors.toList());


    }

    @Override
    public OrganizationDto getOrganizationById(UUID organizationId) {
        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_ORGANIZATION_FOUND));

        List<OrganizationIndustryCode> data = oicRepository.findByOrganization(organizationId);

        List<IndustryCode> ic = oicRepository.findByOrganization(organizationId).stream()
                .map(OrganizationIndustryCode::getIndustryCode)
                .toList();

        OrganizationDto dto = organizationConverter.modelToDto(o);
        dto.setIndustryCodes(ic.stream().map(icConverter::modelToDto).toList());
        return dto;
    }

    @Override
    public InviteUserOrganizationDto removeMember(UUID userId, UUID userOrganizationId) {
        UserOrganization uo = userOrganizationRepository.findById(userOrganizationId)
                .orElseThrow(() -> CustomExceptions.notFound("Member do not belong to this organization"));

        UserOrganization organizationManager = userOrganizationRepository.findByUserAndOrganization(uo.getOrganization().getId(), userId)
                .orElseThrow(() -> CustomExceptions.unauthorized("You do not belong to this organization."));

        if(!Objects.equals(organizationManager.getRole().getName(), Constants.ORGANIZATION_MANAGER)){
            throw CustomExceptions.unauthorized("Your role not support this action");
        }


        if(Objects.equals(uo.getRole().getName(), Constants.ORGANIZATION_MANAGER)){
            throw CustomExceptions.unauthorized("Organization Manager cannot out organization");
        }

        uo.setStatus(false);
        userOrganizationRepository.save(uo);
        return uoConverter.modelToDto(uo);
    }

    @Override
    public List<String> leaveOrganization(UUID userId, UUID userOrganizationId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        UserOrganization uo = userOrganizationRepository.findById(userOrganizationId)
                .orElseThrow(() -> CustomExceptions.notFound("Member do not belong to this organization"));

        if(!uo.getUser().getId().equals(userId)){
            throw CustomExceptions.unauthorized("User not equals to out organization");
        }

        if(Objects.equals(uo.getRole().getName(), Constants.ORGANIZATION_MANAGER)){
            throw CustomExceptions.unauthorized("Organization Manager cannot out organization");
        }


        uo.setStatus(false);
        userOrganizationRepository.save(uo);
        return Collections.emptyList();
    }
}
