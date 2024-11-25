package com.example.cabonerfbe.services;

import java.util.UUID;

public interface EmailService {
    void sendMailCreateOrganization(UUID organizationId);

    void sendMailCreateAccountByOrganizationManager(UUID userId);

    void sendMailRegister(UUID userId);
}
