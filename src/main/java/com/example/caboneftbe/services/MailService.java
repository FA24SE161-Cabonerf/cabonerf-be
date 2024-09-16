package com.example.caboneftbe.services;

import com.example.caboneftbe.request.MailRequest;

import java.util.Map;

public interface MailService {
    String sendMailRegister(MailRequest request, Map<String,Object> model);
}
