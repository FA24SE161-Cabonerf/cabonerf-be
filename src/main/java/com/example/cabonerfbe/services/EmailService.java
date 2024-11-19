package com.example.cabonerfbe.services;

import com.example.cabonerfbe.response.SendMailCreateAccountResponse;
import com.example.cabonerfbe.response.SendMailCreateOrganizationResponse;
import com.example.cabonerfbe.response.SendMailRegisterResponse;

import java.util.UUID;

public interface EmailService {
    SendMailCreateOrganizationResponse sendMailCreateOrganization(UUID organizationId);
    SendMailCreateAccountResponse sendMailCreateAccountByOrganizationManager(UUID userId);
    SendMailRegisterResponse sendMailRegister(UUID userId);
}
