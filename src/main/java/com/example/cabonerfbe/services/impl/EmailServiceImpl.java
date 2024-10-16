package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.services.EmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

//    private final SesClient sesClient;
//    private final String fromAddress;
//    private final String verifyEmailTemplate;
//    private final String clientUrl;
//
//    public EmailServiceImpl() {
////
////        Dotenv dotenv = Dotenv.load();
////
////        String awsRegion = dotenv.get("AWS_REGION");
////        String awsAccessKeyId = dotenv.get("AWS_ACCESS_KEY_ID");
////        String awsSecretAccessKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
////        this.fromAddress = dotenv.get("SES_FROM_ADDRESS");
////        this.clientUrl = dotenv.get("CLIENT_URL");
////
////        // Initialize SES client with credentials from .env
////        this.sesClient = SesClient.builder()
////                .region(Region.of(awsRegion))
////                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey)))
////                .build();
////
////
////        // Read the email template from resources
////        try {
////            ClassPathResource resource = new ClassPathResource("templates/verify-email.html");
////            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
////                this.verifyEmailTemplate = reader.lines().collect(Collectors.joining("\n"));
////            }
////        } catch (IOException e) {
////            throw new UncheckedIOException("Failed to read email template", e);
////        }
//    }
//
//    private void sendEmail(String toAddress, String subject, String body) {
////        Destination destination = Destination.builder()
////                .toAddresses(toAddress)
////                .build();
////
////        Content subjectContent = Content.builder()
////                .data(subject)
////                .charset("UTF-8")
////                .build();
////
////        Content htmlContent = Content.builder()
////                .data(body)
////                .charset("UTF-8")
////                .build();
////
////        Body emailBody = Body.builder()
////                .html(htmlContent)
////                .build();
////
////        Message message = Message.builder()
////                .subject(subjectContent)
////                .body(emailBody)
////                .build();
////
////        SendEmailRequest request = SendEmailRequest.builder()
////                .source(fromAddress)
////                .destination(destination)
////                .message(message)
////                .build();
////
////        sesClient.sendEmail(request);
//    }
//
//    @Override
//    public void sendVerifyRegisterEmail(String toAddress, String emailVerifyToken) {
//        String subject = "Verify your email";
//        String body = verifyEmailTemplate
//                .replace("{{action_url}}", clientUrl + "/verify-email-token?token=" + emailVerifyToken);
//
//        sendEmail(toAddress, subject, body);
//    }
//
//    @Override
//    public void sendForgotPasswordEmail(String toAddress, String forgotPasswordToken) {
//        String subject = "Forgot Password";
//        String body = verifyEmailTemplate
//                .replace("{{title}}", "You requested to reset your password")
//                .replace("{{content}}", "Click the button below to reset your password")
//                .replace("{{titleLink}}", "Reset Password")
//                .replace("{{link}}", clientUrl + "/forgot-password?token=" + forgotPasswordToken);
//
//        sendEmail(toAddress, subject, body);
//    }
}
