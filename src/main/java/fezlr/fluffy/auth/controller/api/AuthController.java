package fezlr.fluffy.auth.controller.api;

import fezlr.fluffy.auth.dto.request.CodeTokenRequest;
import fezlr.fluffy.auth.dto.request.ResetPasswordRequest;
import fezlr.fluffy.auth.dto.request.SendResetPasswordRequest;
import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.auth.service.AuthService;
import fezlr.fluffy.user.dto.request.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    //create a user and send email
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> save(@RequestBody @Valid UserRequest request) {
        log.info("Called create() with BODY = {}", request);
        return ResponseEntity.ok(authService.save(request));
    }

    // send link to an email
    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> sendResetPassword(@RequestBody @Valid SendResetPasswordRequest request) {
        log.info("Called sendResetPassword with BODY = {}", request);
        return ResponseEntity.ok(authService.sendResetPassword(request));
    }

    //when entering the page and getting token
    @GetMapping("/confirm-token")
    public ResponseEntity<AuthResponse> validateResetPasswordToken(@RequestParam String token) {
        log.info("Called validateResetPasswordToken with BODY = {}", token);
        return ResponseEntity.ok(authService.validateResetPasswordToken(token));
    }

    //when enter new password in site
    @PatchMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordRequest request, @RequestParam String token) {
        log.info("Called resetPassword with BODY = {}", request);
        return ResponseEntity.ok(authService.resetPassword(request, token));
    }

    //when enter 6-digit code
    @PostMapping("/confirm-code")
    public ResponseEntity<AuthResponse> validateCodeToken(@RequestBody CodeTokenRequest request) {
        log.info("Called validateCodeToken with BODY = {}", request);
        return ResponseEntity.ok(authService.validateCodeToken(request));
    }
}