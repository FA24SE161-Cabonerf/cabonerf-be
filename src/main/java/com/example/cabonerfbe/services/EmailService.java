package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.MailRequest;

import java.util.Map;
import java.util.UUID;

/**
 * The interface Email service.
 *
 * @author SonPHH.
 */
public interface EmailService {
    /**
     * Sends an email for creating an organization account.
     *
     * @param request the mail request containing recipient details and email metadata
     * @param model   the data model for populating the email template
     */
    void sendMailCreateAccountOrganization(MailRequest request, Map<String, Object> model);

    /**
     * Sends an email for creating an organization account.
     *
     * @param request the mail request containing recipient details and email metadata
     * @param model   the data model for populating the email template
     */
    void sendMailRegister(MailRequest request, Map<String, Object> model);

    /**
     * Sends an email for creating an organization account.
     *
     * @param request the mail request containing recipient details and email metadata
     * @param model   the data model for populating the email template
     */
    void sendMailInviteOrganization(MailRequest request, Map<String, Object> model);
}
