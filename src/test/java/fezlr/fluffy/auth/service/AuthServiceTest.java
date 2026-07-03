package fezlr.fluffy.auth.service;

import fezlr.fluffy.auth.config.AuthPropertiesMessages;
import fezlr.fluffy.auth.dto.request.CodeTokenRequest;
import fezlr.fluffy.auth.dto.request.RegisterRequest;
import fezlr.fluffy.auth.dto.request.ResetPasswordRequest;
import fezlr.fluffy.auth.dto.request.SendResetPasswordRequest;
import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.auth.dto.response.RegisterResponse;
import fezlr.fluffy.mail.property.MailPropertiesMessages;
import fezlr.fluffy.mail.service.MailService;
import fezlr.fluffy.profile.entity.ProfileEntity;
import fezlr.fluffy.profile.service.ProfileService;
import fezlr.fluffy.token.entity.TokenEntity;
import fezlr.fluffy.token.enums.TokenType;
import fezlr.fluffy.token.service.TokenService;
import fezlr.fluffy.token.repository.TokenRepository;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.repository.UserRepository;
import fezlr.fluffy.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthPropertiesMessages authPropertiesMessages;
    @Mock
    private UserService userService;
    @Mock
    private ProfileService profileService;
    @Mock
    private TokenService tokenService;
    @Mock
    private MailService mailService;
    @Mock
    private MailPropertiesMessages mailPropertiesMessages;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void save_shouldCreateUser_andSendCode() {
        RegisterRequest request = new RegisterRequest(
                "test@mail.com",
                "user",
                "12345678",
                "12345678"
        );

        UserRequest userRequest = new UserRequest("test@mail.com", "user", "12345678");

        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("test@mail.com")
                .username("user")
                .password("encoded")
                .build();

        ProfileEntity profile = ProfileEntity.builder()
                .id(1L)
                .user(user)
                .build();

        TokenEntity token = TokenEntity.builder()
                .token("token123")
                .build();

        when(userService.createRequest(request)).thenReturn(userRequest);
        when(userService.create(userRequest)).thenReturn(user);
        when(profileService.create(user)).thenReturn(profile);
        when(tokenService.createCode(user, TokenType.CREATE_USER)).thenReturn(token);

        RegisterResponse response = authService.save(request);

        verify(userService).save(user);
        verify(tokenService).save(token);
        verify(mailService).sendCode("test@mail.com", token.getCode());

        assertEquals("token123", response.token());
    }

    @Test
    void resetPassword_shouldWorkSuccessfully() {
        ResetPasswordRequest request = new ResetPasswordRequest(
                "token",
                "newPass123",
                "newPass123"
        );

        UserEntity user = UserEntity.builder()
                .id(1L)
                .password("oldEncoded")
                .isEnabled(true)
                .build();

        TokenEntity token = TokenEntity.builder()
                .token("token")
                .user(user)
                .isActive(true)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(tokenService.findByToken("token")).thenReturn(token);
        when(userService.getByToken(token)).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        AuthResponse response = authService.resetPassword(request, "token");

        verify(userService).changePassword(user, "newPass123");
        verify(tokenService).deactivate(token);
        verify(tokenService).confirm(token);
        verify(tokenService).save(token);
        verify(userService).save(user);
    }

    @Test
    void validateResetPasswordToken_shouldCallTokenService() {
        when(authPropertiesMessages.resetAllowed()).thenReturn("ok");

        AuthResponse response = authService.validateResetPasswordToken("abc");

        verify(tokenService).validate("abc");
        assertEquals("ok", response.authMessage());
    }

    @Test
    void sendResetPassword_shouldSendEmail_andCreateToken() {
        SendResetPasswordRequest request =
                new SendResetPasswordRequest("test@mail.com");

        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("test@mail.com")
                .isEnabled(true)
                .build();

        TokenEntity token = TokenEntity.builder()
                .token("token123")
                .build();

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(tokenService.createLink(user, TokenType.RESET_PASSWORD))
                .thenReturn(token);

        when(mailPropertiesMessages.linkSubjectMessage())
                .thenReturn("subject");

        when(mailPropertiesMessages.linkMessage())
                .thenReturn("message");

        AuthResponse response = authService.sendResetPassword(request);

        verify(tokenService).save(token);
        verify(mailService).sendLink(eq("test@mail.com"), any(), any(), any());
    }

    @Test
    void validateCodeToken_shouldEnableUser_andConfirmToken() {
        CodeTokenRequest request =
                new CodeTokenRequest("token", "123456");

        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("test@mail.com")
                .build();

        TokenEntity token = TokenEntity.builder()
                .token("token")
                .code("123456")
                .user(user)
                .isActive(true)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(tokenRepository.findByTokenWithUser("token"))
                .thenReturn(Optional.of(token));

        when(tokenService.validate("token", "test@mail.com"))
                .thenReturn(token);

        when(userService.getByToken(token))
                .thenReturn(user);

        AuthResponse response = authService.validateCodeToken(request);

        verify(userService).save(user);
        verify(tokenService).save(token);
    }

    @Test
    void save_shouldThrowException_whenPasswordsDoNotMatch() {
        RegisterRequest request = new RegisterRequest(
                "test@mail.com",
                "user",
                "12345678",
                "87654321"
        );

        assertThrows(IllegalArgumentException.class,
                () -> authService.save(request));

        verifyNoInteractions(userService);
        verifyNoInteractions(tokenService);
        verifyNoInteractions(mailService);
    }

    @Test
    void sendResetPassword_shouldThrowException_whenUserIsDisabled() {
        SendResetPasswordRequest request =
                new SendResetPasswordRequest("test@mail.com");

        UserEntity user = UserEntity.builder()
                .email("test@mail.com")
                .isEnabled(false)
                .build();

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class,
                () -> authService.sendResetPassword(request));

        verify(tokenService, never()).createLink(any(), any());
    }

    @Test
    void validateResetPasswordToken_shouldReturnResponse() {
        when(authPropertiesMessages.resetAllowed())
                .thenReturn("Allowed");

        AuthResponse response =
                authService.validateResetPasswordToken("token");

        verify(tokenService).validate("token");

        assertEquals("Allowed", response.authMessage());
    }

    @Test
    void resetPassword_shouldThrowException_whenPasswordsAreEqualToOldPassword() {
        ResetPasswordRequest request =
                new ResetPasswordRequest(
                        "token",
                        "newPassword",
                        "newPassword"
                );

        UserEntity user = UserEntity.builder()
                .password("encoded")
                .isEnabled(true)
                .build();

        TokenEntity token = TokenEntity.builder()
                .token("token")
                .isActive(true)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(tokenService.findByToken("token"))
                .thenReturn(token);

        when(userService.getByToken(token))
                .thenReturn(user);

        when(passwordEncoder.matches("newPassword", "encoded"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> authService.resetPassword(request, "token"));

        verify(userService, never()).changePassword(any(), any());
    }

    @Test
    void validateCodeToken_shouldThrowException_whenCodeIsIncorrect() {
        CodeTokenRequest request =
                new CodeTokenRequest("token", "123456");

        UserEntity user = UserEntity.builder()
                .email("test@mail.com")
                .build();

        TokenEntity token = TokenEntity.builder()
                .token("token")
                .code("654321")
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .isActive(true)
                .build();

        when(tokenRepository.findByTokenWithUser("token"))
                .thenReturn(Optional.of(token));

        when(tokenService.validate("token", "test@mail.com"))
                .thenReturn(token);

        assertThrows(IllegalArgumentException.class,
                () -> authService.validateCodeToken(request));

        verify(userService, never()).save(any());
    }

    @Test
    void sendResetPassword_shouldThrowException_whenUserNotFound() {
        SendResetPasswordRequest request =
                new SendResetPasswordRequest("notfound@mail.com");

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> authService.sendResetPassword(request));

        verifyNoInteractions(tokenService);
        verifyNoInteractions(mailService);
    }

    @Test
    void resetPassword_shouldThrowException_whenUserIsDisabled() {
        UserEntity user = UserEntity.builder()
                .isEnabled(false)
                .password("encoded")
                .build();

        TokenEntity token = TokenEntity.builder()
                .token("token")
                .user(user)
                .isActive(true)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        ResetPasswordRequest request =
                new ResetPasswordRequest("token", "newPass123", "newPass123");

        when(tokenService.findByToken("token"))
                .thenReturn(token);

        when(userService.getByToken(token))
                .thenReturn(user);

        assertThrows(IllegalArgumentException.class,
                () -> authService.resetPassword(request, "token"));

        verify(userService, never()).changePassword(any(), any());
    }

    @Test
    void resetPassword_shouldThrowException_whenTokenInactive() {
        UserEntity user = UserEntity.builder()
                .isEnabled(true)
                .password("encoded")
                .build();

        TokenEntity token = TokenEntity.builder()
                .token("token")
                .user(user)
                .isActive(false)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        ResetPasswordRequest request =
                new ResetPasswordRequest("token", "newPass123", "newPass123");

        when(tokenService.findByToken("token"))
                .thenReturn(token);

        when(userService.getByToken(token))
                .thenReturn(user);

        assertThrows(IllegalArgumentException.class,
                () -> authService.resetPassword(request, "token"));

        verify(userService, never()).changePassword(any(), any());
    }

    @Test
    void validateCodeToken_shouldThrowException_whenTokenNotFound() {
        CodeTokenRequest request =
                new CodeTokenRequest("token", "123456");

        when(tokenRepository.findByTokenWithUser("token"))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> authService.validateCodeToken(request));

        verifyNoInteractions(tokenService);
        verifyNoInteractions(userService);
    }

    @Test
    void validateCodeToken_shouldThrowException_whenCodeIsCorrectButTokenMismatch() {
        UserEntity user = UserEntity.builder()
                .email("test@mail.com")
                .build();

        TokenEntity token = TokenEntity.builder()
                .token("token")
                .code("123456")
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .isActive(true)
                .build();

        CodeTokenRequest request =
                new CodeTokenRequest("token", "123456");

        when(userService.getByToken(token))
                .thenReturn(user);

        when(tokenRepository.findByTokenWithUser("token"))
                .thenReturn(Optional.of(token));

        when(tokenService.validate("token", "test@mail.com"))
                .thenReturn(token);

        assertDoesNotThrow(() -> authService.validateCodeToken(request));

        verify(userService).save(any(UserEntity.class));
        verify(tokenService).save(any(TokenEntity.class));
    }
}