package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Organization;
import com.example.cabonerfbe.models.UserOrganization;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.repositories.OrganizationRepository;
import com.example.cabonerfbe.repositories.UserOrganizationRepository;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.response.SendMailCreateAccountResponse;
import com.example.cabonerfbe.response.SendMailCreateOrganizationResponse;
import com.example.cabonerfbe.response.SendMailRegisterResponse;
import com.example.cabonerfbe.services.EmailService;
import com.example.cabonerfbe.services.JwtService;
import com.example.cabonerfbe.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserOrganizationRepository uoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public SendMailCreateOrganizationResponse sendMailCreateOrganization(UUID organizationId) {
        // Kiểm tra organization tồn tại
        organizationRepository.findById(organizationId)
                .orElseThrow(() -> CustomExceptions.notFound("Organization not exist"));

        List<UserOrganization> userOrganizations = uoRepository.findByOrganization(organizationId);
        if (userOrganizations.isEmpty()) {
            throw CustomExceptions.badRequest("Account not exist");
        }
        if (userOrganizations.size() > 1) {
            throw CustomExceptions.badRequest("Account already active");
        }

        UserOrganization userOrg = userOrganizations.get(0);
        if (Objects.equals(userOrg.getUser().getUserVerifyStatus().getStatusName(), "Verified")) {
            throw CustomExceptions.badRequest("Account already active");
        }

        String token = jwtService.generateEmailVerifyToken(userOrg.getUser());
        String password = PasswordGenerator.generateRandomPassword(8);

        userRepository.findById(userOrg.getUser().getId())
                .ifPresent(user -> {
                    user.setPassword(password);
                    userRepository.save(user);
                });

        return SendMailCreateOrganizationResponse.builder()
                .organizationId(organizationId)
                .token(token)
                .password(password)
                .build();
    }

    @Override
    public SendMailCreateAccountResponse sendMailCreateAccountByOrganizationManager(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound("User not found"));

        String token = jwtService.generateEmailVerifyToken(u);

        String password = PasswordGenerator.generateRandomPassword(8);
        u.setPassword(password);
        userRepository.save(u);

        return SendMailCreateAccountResponse.builder()
                .token(token)
                .password(password)
                .build();
    }

    @Override
    public SendMailRegisterResponse sendMailRegister(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound("User not found"));

        String token = jwtService.generateEmailVerifyToken(u);

        return SendMailRegisterResponse.builder()
                .token(token)
                .build();
    }
}
