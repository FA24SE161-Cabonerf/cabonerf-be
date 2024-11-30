package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.SendMailInviteResponse;
import com.example.cabonerfbe.response.SendMailRegisterResponse;

import java.util.UUID;

public interface EmailService {
    void sendMailCreateOrganization(UUID organizationId);

    void sendMailCreateAccountByOrganizationManager(UUID userId);

    void sendMailRegister(UUID userId);

    void sendMailInviteOrganization(UUID userId, UUID organizationId);
}
