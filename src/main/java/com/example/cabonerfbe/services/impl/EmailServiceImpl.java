package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.repositories.OrganizationRepository;
import com.example.cabonerfbe.repositories.UserOrganizationRepository;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.request.MailRequest;
import com.example.cabonerfbe.request.SendMailCreateAccountOrganizationRequest;
import com.example.cabonerfbe.request.SendMailInviteRequest;
import com.example.cabonerfbe.services.EmailService;
import com.example.cabonerfbe.services.JwtService;
import com.example.cabonerfbe.services.MessagePublisher;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The class Email service.
 *
 * @author SonPHH.
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    /**
     * The constant ORGANIZATION_NAME.
     */
    public static final String ORGANIZATION_NAME = "organization_name";
    /**
     * The constant EMAIL.
     */
    public static final String EMAIL = "email";
    /**
     * The constant PASSWORD.
     */
    public static final String PASSWORD = "password";
    /**
     * The Message publisher.
     */
    @Autowired
    MessagePublisher messagePublisher;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserOrganizationRepository uoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mail;
    @Autowired
    private Configuration config;

    @Override
    public void sendMailCreateAccountOrganization(MailRequest request, Map<String, Object> model) {
        MimeMessage message = mail.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template t = config.getTemplate("create_organization.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            mail.send(message);
        } catch (MessagingException | IOException | TemplateException ignored) {
        }
    }

    @Override
    public void sendMailRegister(MailRequest request, Map<String, Object> model) {

    }

    @Override
    public void sendMailInviteOrganization(MailRequest request, Map<String, Object> model) {
        MimeMessage message = mail.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template t = config.getTemplate("invite.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            mail.send(message);
        } catch (MessagingException | IOException | TemplateException ignored) {
        }
    }

    /**
     * Consume send mail invite to organization method.
     *
     * @param request the request
     */
    @RabbitListener(queues = RabbitMQConfig.EMAIL_INVITE_QUEUE)
    public void consumeSendMailInviteToOrganization(SendMailInviteRequest request) {
        log.info("consumeSendMailInviteToOrganization: {}", request);
        MailRequest mailRequest = new MailRequest();
        mailRequest.setSubject(Constants.INVITE_ORGANIZATION_SUBJECT);
        mailRequest.setName(Constants.CABONERF_TITLE);
        mailRequest.setTo(request.getUserEmail());
        mailRequest.setFrom(Constants.CABONERF_GMAIL);

        Map<String, Object> model = Map.of(
                ORGANIZATION_NAME, request.getOrganizationName()
        );
        sendMailInviteOrganization(mailRequest, model);
    }

    /**
     * Consume send mail create account organization method.
     *
     * @param request the request
     */
    @RabbitListener(queues = RabbitMQConfig.EMAIL_INVITE_QUEUE)
    public void consumeSendMailCreateAccountOrganization(SendMailCreateAccountOrganizationRequest request) {
        log.info("consumeSendMailCreateAccountOrganization: {}", request);
        MailRequest mailRequest = new MailRequest();
        mailRequest.setSubject(Constants.CREATE_ORGANIZATION_SUBJECT);
        mailRequest.setName(Constants.CABONERF_TITLE);
        mailRequest.setTo(request.getEmail());
        mailRequest.setFrom(Constants.CABONERF_GMAIL);

        Map<String, Object> model = new HashMap<>();
        model.put(EMAIL, request.getEmail());
        model.put(PASSWORD, request.getPassword());

        sendMailCreateAccountOrganization(mailRequest, model);
    }
}
