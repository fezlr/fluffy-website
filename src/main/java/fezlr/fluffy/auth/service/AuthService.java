package fezlr.fluffy.auth.service;

import fezlr.fluffy.auth.config.AuthPropertiesMessages;
import fezlr.fluffy.auth.dto.request.CodeTokenRequest;
import fezlr.fluffy.auth.dto.request.RegisterRequest;
import fezlr.fluffy.auth.dto.request.ResetPasswordRequest;
import fezlr.fluffy.auth.dto.request.SendResetPasswordRequest;
import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.auth.dto.response.RegisterResponse;
import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.mail.property.MailPropertiesMessages;
import fezlr.fluffy.mail.service.MailService;
import fezlr.fluffy.profile.entity.ProfileEntity;
import fezlr.fluffy.profile.service.ProfileService;
import fezlr.fluffy.token.entity.TokenEntity;
import fezlr.fluffy.token.enums.TokenType;
import fezlr.fluffy.token.repository.TokenRepository;
import fezlr.fluffy.token.service.TokenService;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.enums.Role;
import fezlr.fluffy.user.repository.UserRepository;
import fezlr.fluffy.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthPropertiesMessages authPropertiesMessages;
    private final CustomAuthService customAuthService;
    private final UserService userService;
    private final ProfileService profileService;
    private final TokenService tokenService;
    private final MailService mailService;
    private final MailPropertiesMessages mailPropertiesMessages;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse save(RegisterRequest request) {
        if(!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        UserRequest userRequest = userService.createRequest(request);
        UserEntity userEntity = userService.create(userRequest);
        ProfileEntity profileEntity = profileService.create(userEntity);

        userEntity.setProfile(profileEntity);

        TokenEntity tokenEntity = tokenService.createCode(userEntity, TokenType.CREATE_USER);

        userService.save(userEntity);
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

        mailService.sendLink(request.email(), tokenEntity.getToken(), mailPropertiesMessages.linkSubjectMessage(), mailPropertiesMessages.linkMessage());
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
        UserEntity userEntity = userService.getByToken(tokenEntity);

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
        TokenEntity token = tokenRepository
                .findByTokenWithUser(request.token())
                .orElseThrow(() -> new EntityNotFoundException("Token is not found"));

        TokenEntity tokenEntity = tokenService.validate(request.token(), token.getUser().getEmail());

        if(!tokenEntity.getCode().equals(request.code())) {
            throw new IllegalArgumentException("Code is incorrect");
        }

        if(!tokenEntity.getToken().equals(request.token())) {
            throw new IllegalArgumentException("Token is incorrect");
        }

        UserEntity userEntity = userService.getByToken(tokenEntity);
        userEntity.setEnabled(true);

        tokenService.deactivate(tokenEntity);
        tokenService.confirm(tokenEntity);

        tokenService.save(tokenEntity);
        userService.save(userEntity);
        return new AuthResponse(authPropertiesMessages.codeConfirmed());
    }

    @Transactional
    public AuthResponse sendConfirmEmail(Long id, String newEmail) {
        UserEntity user = userService.findById(id);

        if (user.getEmail().equals(newEmail)) {
            throw new IllegalStateException("Email must be different");
        }

        if (!user.isEnabled()) {
            throw new IllegalStateException("User is not enabled");
        }

        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalStateException("Email must be unique");
        }

        if (!customAuthService.getCurrentUser().getId().equals(id) && user.getRole() != Role.ADMIN) {
            throw new IllegalStateException("It must be your account or you must be an admin");
        }

        TokenEntity token = tokenService.createLink(user, TokenType.EMAIL_VERIFICATION);

        token.setNewEmail(newEmail);

        mailService.sendLink(newEmail, token.getToken(), "Email verification", mailPropertiesMessages.resetEmailMessage());

        tokenService.save(token);

        return new AuthResponse(authPropertiesMessages.resetEmail());
    }

    //confirm email verification
    @Transactional
    public AuthResponse validateEmailVerificationToken(String tokenString) {
        TokenEntity token = tokenService.validate(tokenString);
        UserEntity user = userService.getByToken(token);

        user.setEmail(token.getNewEmail());

        tokenService.deactivate(token);
        tokenService.confirm(token);
        tokenService.save(token);

        return new AuthResponse(authPropertiesMessages.allowed());
    }
}