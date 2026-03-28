package fezlr.fluffy.auth.service;

import fezlr.fluffy.auth.dto.request.ResetPasswordRequest;
import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.mail.service.MailService;
import fezlr.fluffy.token.dto.response.TokenResponse;
import fezlr.fluffy.token.service.TokenService;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.repository.UserRepository;
import fezlr.fluffy.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    @Value("${spring.auth.create.message}")
    private String authMessage;
    @Value("${spring.auth.reset-password.message}")
    private String resetPasswordMessage;
    private final UserService userService;
    private final TokenService tokenService;
    private final MailService mailService;
    private final UserRepository userRepository;

    @Transactional
    public AuthResponse save(UserRequest userRequest) {
        UserEntity entity = userService.save(userRequest);
        TokenResponse tokenResponse = tokenService.saveCode(entity);
        mailService.sendCode(userRequest.email(), tokenResponse.token());
        return new AuthResponse(authMessage);
    }

    @Transactional
    public AuthResponse resetPassword(ResetPasswordRequest request) {
        UserEntity entity = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        TokenResponse tokenResponse = tokenService.saveLink(entity);
        mailService.sendLink(request.email(), tokenResponse.token());
        return new AuthResponse(resetPasswordMessage);
    }
}