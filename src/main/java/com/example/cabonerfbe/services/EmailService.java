package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.SendMailRegisterResponse;

import java.util.UUID;

public interface EmailService {
    void sendMailCreateOrganization(UUID organizationId);

    void sendMailCreateAccountByOrganizationManager(UUID userId);

    void sendMailRegister(UUID userId);

    SendMailRegisterResponse sendMailInviteOrganization(UUID userId, UUID organizationId);
}
