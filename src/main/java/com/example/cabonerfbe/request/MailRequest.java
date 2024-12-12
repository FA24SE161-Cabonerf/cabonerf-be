package com.example.cabonerfbe.request;

import lombok.Data;

/**
 * The class Mail request.
 *
 * @author SonPHH.
 */
@Data
public class MailRequest {
    private String name;
    private String to;
    private String from;
    private String subject;
}
