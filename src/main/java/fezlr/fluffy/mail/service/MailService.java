package fezlr.fluffy.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    @Value("${spring.mail.name}")
    private String mailName;
    @Value("${spring.mail.username}")
    private String mailAddress;
    @Value("${spring.mail.code.subject}")
    private String mailCodeSubject;
    @Value("${spring.mail.link.subject}")
    private String mailLinkSubject;
    @Value("${spring.mail.code.message}")
    private String mailCodeMessage;
    @Value("${spring.mail.link.message}")
    private String mailLinkMessage;
    @Value("${spring.application.base-url}")
    private String baseUrl;
    private final JavaMailSender mailSender;

    @Async
    public void sendCode(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom("%s <%s>".formatted(mailName, mailAddress));
            message.setSubject(mailCodeSubject);
            message.setText(mailCodeMessage.formatted(token));
            mailSender.send(message);
        } catch(MailException e) {
            log.error("Failed to send confirmation code to = {}: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendLink(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom("%s <%s>".formatted(mailName, mailAddress));
            message.setSubject(mailLinkSubject);
            message.setText(mailLinkMessage.formatted(baseUrl, token));
            mailSender.send(message);
        } catch(MailException e) {
            log.error("Failed to send confirmation link to = {}: {}", to, e.getMessage());
        }
    }
}
