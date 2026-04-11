package fezlr.fluffy.auth.service;

import fezlr.fluffy.auth.config.AuthPropertiesMessages;
import fezlr.fluffy.auth.dto.request.CodeTokenRequest;
import fezlr.fluffy.auth.dto.request.RegisterRequest;
import fezlr.fluffy.auth.dto.request.ResetPasswordRequest;
import fezlr.fluffy.auth.dto.request.SendResetPasswordRequest;
import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.auth.dto.response.RegisterResponse;
import fezlr.fluffy.mail.service.MailService;
import fezlr.fluffy.token.entity.TokenEntity;
import fezlr.fluffy.token.enums.TokenType;
import fezlr.fluffy.token.repository.TokenRepository;
import fezlr.fluffy.token.service.TokenService;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.repository.UserRepository;
import fezlr.fluffy.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthPropertiesMessages authPropertiesMessages;
    private final UserService userService;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse save(RegisterRequest request) {

        if(!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        //TODO: make a method and move to UserService
        UserRequest userRequest = new UserRequest(
                request.email(),
                request.username(),
                request.password()
        );

        UserEntity entity = userService.create(userRequest);
        TokenEntity tokenEntity = tokenService.createCode(entity, TokenType.CREATE_USER);
        tokenService.save(tokenEntity);
        mailService.sendCode(request.email(), tokenEntity.getCode());
        return new RegisterResponse(tokenEntity.getToken());
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
        return new AuthResponse(authPropertiesMessages.resetSent());
    }

    @Transactional
    public AuthResponse validateResetPasswordToken(String token) {
        tokenService.validate(token);
        return new AuthResponse(authPropertiesMessages.resetAllowed());
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
        return new AuthResponse(authPropertiesMessages.resetDone());
    }

    @Transactional
    public AuthResponse validateCodeToken(CodeTokenRequest request) {
        //find email by uuid
        var token = tokenRepository
                .findByTokenWithUser(request.token())
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));

        //TODO: refactor(optimize)
        TokenEntity tokenEntity = tokenService.validate(request.token(), token.getUser().getEmail());

        if(!tokenEntity.getCode().equals(request.code())) {
            throw new IllegalArgumentException("Code is incorrect");
        }

        if(!tokenEntity.getToken().equals(request.token())) {
            throw new IllegalArgumentException("Token is incorrect");
        }

        UserEntity userEntity = userService.getUserByToken(tokenEntity);
        userEntity.setEnabled(true);
        tokenService.deactivate(tokenEntity);
        tokenService.confirm(tokenEntity);
        tokenService.save(tokenEntity);
        userService.save(userEntity);
        return new AuthResponse(authPropertiesMessages.codeConfirmed());
    }
}