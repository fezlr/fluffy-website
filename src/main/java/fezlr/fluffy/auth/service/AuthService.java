package fezlr.fluffy.auth.service;

import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.mail.service.MailService;
import fezlr.fluffy.token.dto.response.TokenResponse;
import fezlr.fluffy.token.service.TokenService;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    @Value("${spring.auth.message}")
    private String authMessage;
    private final UserService userService;
    private final TokenService tokenService;
    private final MailService mailService;

    @Transactional
    public AuthResponse save(UserRequest userRequest) {
        UserEntity entity = userService.save(userRequest);
        TokenResponse tokenResponse = tokenService.save(entity);
        mailService.send(userRequest.email(), tokenResponse.token());
        return new AuthResponse(authMessage);
    }
}