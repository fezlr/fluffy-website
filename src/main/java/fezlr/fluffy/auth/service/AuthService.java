package fezlr.fluffy.auth.service;

import fezlr.fluffy.auth.dto.request.CodeTokenRequest;
import fezlr.fluffy.auth.dto.request.ResetPasswordRequest;
import fezlr.fluffy.auth.dto.request.SendResetPasswordRequest;
import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.mail.service.MailService;
import fezlr.fluffy.token.entity.TokenEntity;
import fezlr.fluffy.token.enums.TokenType;
import fezlr.fluffy.token.service.TokenService;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.repository.UserRepository;
import fezlr.fluffy.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    @Value("${spring.auth.code.message}")
    private String authMessage;
    @Value("${spring.auth.reset-password.send-message}")
    private String resetPasswordSendMessage;
    @Value("${spring.auth.reset-password.message}")
    private String resetPasswordMessage;
    @Value("${spring.auth.reset-password.confirm-token.message}")
    private String resetPasswordConfirmTokenMessage;
    @Value("${spring.auth.code.validate-code-message}")
    private String validateCodeMessage;
    private final UserService userService;
    private final TokenService tokenService;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse save(UserRequest userRequest) {
        UserEntity entity = userService.create(userRequest);
        TokenEntity tokenEntity = tokenService.createCode(entity, TokenType.CREATE_USER);
        tokenService.save(tokenEntity);
        mailService.sendCode(userRequest.email(), tokenEntity.getToken());
        return new AuthResponse(authMessage);
    }

    @Transactional
    public AuthResponse sendResetPassword(SendResetPasswordRequest request) {
        UserEntity entity = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        if(!entity.isEnabled()) {
            throw new IllegalArgumentException("User is not enabled");
        }

        TokenEntity tokenEntity = tokenService.createLink(entity, TokenType.RESET_PASSWORD);
        tokenService.deactivateAllByUserAndTokenType(entity, TokenType.RESET_PASSWORD);
        tokenService.save(tokenEntity);
        mailService.sendLink(request.email(), tokenEntity.getToken());
        return new AuthResponse(resetPasswordSendMessage);
    }

    @Transactional
    public AuthResponse validateResetPasswordToken(String token) {
        tokenService.validate(token);
        return new AuthResponse(resetPasswordConfirmTokenMessage);
    }

    @Transactional
    public AuthResponse resetPassword(ResetPasswordRequest request, String token) {
        if(!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        TokenEntity tokenEntity = tokenService.findByToken(token);
        UserEntity userEntity = userService.getUserByToken(tokenEntity);

        if(passwordEncoder.matches(request.newPassword(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Password must be new");
        }

        if(!userEntity.isEnabled()) {
            throw new IllegalArgumentException("Account must be enabled");
        }

        if(!tokenEntity.isActive()) {
            throw new IllegalArgumentException("Token must be active");
        }

        userService.changePassword(userEntity, request.newPassword());
        tokenService.deactivate(tokenEntity);
        tokenService.confirm(tokenEntity);
        tokenService.save(tokenEntity);
        userService.save(userEntity);
        return new AuthResponse(resetPasswordMessage);
    }

    @Transactional
    public AuthResponse validateCodeToken(CodeTokenRequest request) {
        TokenEntity tokenEntity = tokenService.validate(request.code(), request.email());

        if(!tokenEntity.getToken().equals(request.code())) {
            throw new IllegalArgumentException("Token is incorrect");
        }

        UserEntity userEntity = userService.getUserByToken(tokenEntity);
        userEntity.setEnabled(true);
        tokenService.deactivate(tokenEntity);
        tokenService.confirm(tokenEntity);
        tokenService.save(tokenEntity);
        userService.save(userEntity);
        return new AuthResponse(validateCodeMessage);
    }
}