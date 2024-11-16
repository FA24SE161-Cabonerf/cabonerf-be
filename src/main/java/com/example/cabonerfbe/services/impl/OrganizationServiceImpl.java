package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.OrganizationConverter;
import com.example.cabonerfbe.dto.OrganizationDto;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateOrganizationRequest;
import com.example.cabonerfbe.request.UpdateOrganizationRequest;
import com.example.cabonerfbe.response.GetAllOrganizationResponse;
import com.example.cabonerfbe.response.LoginResponse;
import com.example.cabonerfbe.services.EmailService;
import com.example.cabonerfbe.services.OrganizationService;
import com.example.cabonerfbe.services.S3Service;
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
    S3Service s3Service;

    @Override
    public GetAllOrganizationResponse getAll(int pageCurrent, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageCurrent - 1, pageSize);


        Page<Organization> data = keyword == null ? organizationRepository.findAll(pageable) : organizationRepository.findAllByKeyword(keyword, pageable);

        int totalPage = data.getTotalPages();

        if(pageCurrent > totalPage){
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
        if(user.isPresent()){
            if(Objects.equals(user.get().getRole().getName(), "Organization Manager")){
                throw CustomExceptions.badRequest("Email already use with other organization");
            }

            user.get().setRole(roleRepository.findByName("Organization Manager").get());
            userRepository.save(user.get());
        }else{
            Users newUser = new Users();
            newUser.setEmail(request.getEmail());
            newUser.setFullName(request.getName());
            newUser.setUserVerifyStatus(userVerifyStatusRepository.findByName("Pending").get());
            newUser.setRole(roleRepository.findByName("Organization Manager").get());
            newUser.setPassword(passwordEncoder.encode(PasswordGenerator.generateRandomPassword(8)));
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

        String urlContract="";
        try{
            String key = "contract/" + contractFile.getOriginalFilename();
            byte[] file = contractFile.getBytes();
            urlContract = s3Service.updateFile(key,file);
        }catch (Exception ignored){

        }

        //storage contract
        Contract c = new Contract();
        c.setOrganization(o);
        c.setUrl(urlContract);
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
    public LoginResponse confirmOrganization(UUID organizationId) {
        Organization o = organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound("Organization not exist"));

        Users owner = userRepository.getOwnerOrganization(organizationId);

        Workspace w = new Workspace();
        w.setName(o.getName());

        return null;
    }
}
