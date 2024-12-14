package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.repositories.OrganizationRepository;
import com.example.cabonerfbe.repositories.UserOrganizationRepository;
import com.example.cabonerfbe.repositories.UserRepository;
import com.example.cabonerfbe.request.MailRequest;
import com.example.cabonerfbe.services.EmailService;
import com.example.cabonerfbe.services.JwtService;
import com.example.cabonerfbe.services.MessagePublisher;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import freemarker.template.*;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * The class Email service.
 *
 * @author SonPHH.
 */
@Service
public class EmailServiceImpl implements EmailService {

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
            MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template t = config.getTemplate("create_organization.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t,model);

            helper.setTo(request.getTo());
            helper.setText(html,true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            mail.send(message);
        }catch (MessagingException | IOException | TemplateException ignored){
        }
    }

    @Override
    public void sendMailRegister(MailRequest request, Map<String, Object> model) {

    }

    @Override
    public void sendMailInviteOrganization(MailRequest request, Map<String, Object> model) {
        MimeMessage message = mail.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template t = config.getTemplate("invite.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t,model);

            helper.setTo(request.getTo());
            helper.setText(html,true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            mail.send(message);
        }catch (MessagingException | IOException | TemplateException ignored){
        }
    }
}
