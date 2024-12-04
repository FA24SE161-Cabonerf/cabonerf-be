package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.UserOrganization;
import com.example.cabonerfbe.models.Users;
import com.example.cabonerfbe.repositories.OrganizationRepository;
import com.example.cabonerfbe.repositories.UserOrganizationRepository;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.response.SendMailCreateAccountOrganizationResponse;
import com.example.cabonerfbe.response.SendMailInviteResponse;
import com.example.cabonerfbe.response.SendMailRegisterResponse;
import com.example.cabonerfbe.services.EmailService;
import com.example.cabonerfbe.services.JwtService;
import com.example.cabonerfbe.services.MessagePublisher;
import com.example.cabonerfbe.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Autowired
    MessagePublisher messagePublisher;

    @Override
    public void sendMailCreateAccountOrganization(UUID userId) {
        // Kiểm tra organization tồn tại
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));
        String password = PasswordGenerator.generateRandomPassword(8);;

        u.setPassword(password);

        String token = jwtService.generateEmailVerifyToken(u);
        userRepository.save(u);

        // build the response
        SendMailCreateAccountOrganizationResponse createOrganizationResponse = SendMailCreateAccountOrganizationResponse.builder()
                .token(token)
                .email(u.getEmail())
                .password(password)
                .build();

        // publish the msg to rabbit queue
        messagePublisher.publishSendMailCreateAccountOrganization(createOrganizationResponse);
    }
//
//    @Override
//    public void sendMailCreateAccountByOrganizationManager(UUID userId) {
//        Users u = userRepository.findById(userId)
//                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));
//
//        String token = jwtService.generateEmailVerifyToken(u);
//
//        String password = PasswordGenerator.generateRandomPassword(8);
//        u.setPassword(password);
//        userRepository.save(u);
//
//        SendMailCreateAccountResponse createAccountResponse = SendMailCreateAccountResponse.builder()
//                .token(token)
//                .email(u.getEmail())
//                .password(password)
//                .build();
//
//        messagePublisher.publishSendMailCreateAccountByOrganizationManager(createAccountResponse);
//    }

    @Override
    public void sendMailRegister(UUID userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        String token = jwtService.generateEmailVerifyToken(u);

        SendMailRegisterResponse registerResponse = SendMailRegisterResponse.builder()
                .token(token)
                .email(u.getEmail())
                .build();

        messagePublisher.publishSendMailRegister(registerResponse);
    }

    @Override
    public void sendMailInviteOrganization(UUID userId, UUID userOrganizationId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        UserOrganization uo = uoRepository.findById(userOrganizationId)
                .orElseThrow(() -> CustomExceptions.unauthorized(MessageConstants.USER_NOT_HAVE_INVITE_ORGANIZATION));

        String token = jwtService.generateEmailVerifyToken(u);

        SendMailInviteResponse response = SendMailInviteResponse.builder()
                .token(token)
                .email(u.getEmail())
                .build();
    }


}
