package fezlr.fluffy.auth.controller.api;

import fezlr.fluffy.auth.dto.request.ResetPasswordRequest;
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

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> save(@RequestBody @Valid UserRequest request) {
        log.info("Called save() with BODY = {}", request);
        return ResponseEntity.ok(authService.save(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        log.info("Called resetPassword with BODY = {}", request);
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}