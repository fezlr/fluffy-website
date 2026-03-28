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
    @Value("${spring.mail.subject}")
    private String mailSubject;
    @Value("${spring.mail.message}")
    private String mailMessage;
    private final JavaMailSender mailSender;

    @Async
    public void send(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom("%s <%s>".formatted(mailName, mailAddress));
            message.setSubject(mailSubject);
            message.setText(mailMessage.formatted(token));
            mailSender.send(message);
        } catch(MailException e) {
            log.error("Failed to send confirmation code to = {}: {}", to, e.getMessage());
        }
    }
}
