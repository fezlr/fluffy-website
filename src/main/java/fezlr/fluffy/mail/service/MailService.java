package fezlr.fluffy.mail.service;

import fezlr.fluffy.common.property.CommonProperties;
import fezlr.fluffy.mail.property.MailProperties;
import fezlr.fluffy.mail.property.MailPropertiesMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    private final MailProperties mailProperties;
    private final MailPropertiesMessages mailPropertiesMessages;
    private final CommonProperties commonProperties;
    private final JavaMailSender mailSender;

    @Async
    public void sendCode(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom("%s <%s>".formatted(mailProperties.name(), mailProperties.username()));
            message.setSubject(mailPropertiesMessages.codeSubjectMessage());
            message.setText(mailPropertiesMessages.codeMessage().formatted(token));
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
            message.setFrom("%s <%s>".formatted(mailProperties.name(), mailProperties.username()));
            message.setSubject(mailPropertiesMessages.linkSubjectMessage());
            message.setText(mailPropertiesMessages.linkMessage().formatted(commonProperties.baseURL(), token));
            mailSender.send(message);
        } catch(MailException e) {
            log.error("Failed to send confirmation link to = {}: {}", to, e.getMessage());
        }
    }
}