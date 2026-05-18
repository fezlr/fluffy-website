package fezlr.fluffy.auth.controller.api;

import fezlr.fluffy.auth.dto.request.*;
import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.auth.dto.response.RegisterResponse;
import fezlr.fluffy.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    //create a user and send email
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> save(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.save(request));
    }

    //send link to an email
    @PostMapping("/send-reset-password")
    public ResponseEntity<AuthResponse> sendResetPassword(@RequestBody @Valid SendResetPasswordRequest request) {
        return ResponseEntity.ok(authService.sendResetPassword(request));
    }

    //when put new password in site
    @PatchMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request, request.token()));
    }

    //when put 6-digit code
    @PostMapping("/confirm-code")
    public ResponseEntity<AuthResponse> validateCodeToken(@RequestBody @Valid CodeTokenRequest request) {
        return ResponseEntity.ok(authService.validateCodeToken(request));
    }

    //when changing email address
    @PostMapping("/confirm-email/{id}")
    public ResponseEntity<AuthResponse> sendConfirmEmail(@PathVariable Long id, @RequestBody @Valid String newEmail) {
        return ResponseEntity.ok(authService.sendConfirmEmail(id, newEmail));
    }
}