package com.example.cabonerfbe.services;

import com.example.cabonerfbe.request.MailRequest;

import java.util.Map;

public interface EmailService {
    String sendMailCreateOrganization(MailRequest request, Map<String, Object> model);
}
