package com.example.cabonerfbe.services;

import java.util.UUID;

/**
 * The interface Email service.
 *
 * @author SonPHH.
 */
public interface EmailService {
    /**
     * Send mail create account organization method.
     *
     * @param userId the user id
     */
    void sendMailCreateAccountOrganization(UUID userId);

    /**
     * Send mail register method.
     *
     * @param userId the user id
     */
    void sendMailRegister(UUID userId);

    /**
     * Send mail invite organization method.
     *
     * @param userId         the user id
     * @param organizationId the organization id
     */
    void sendMailInviteOrganization(UUID userId, UUID organizationId);
}
