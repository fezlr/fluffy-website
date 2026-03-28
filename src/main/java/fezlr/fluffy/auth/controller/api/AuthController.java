package fezlr.fluffy.auth.controller.api;

import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.auth.service.AuthService;
import fezlr.fluffy.user.dto.request.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> save(@RequestBody @Valid UserRequest user) {
        log.info("Called saveUser() with BODY = {}", user);
        return ResponseEntity.ok(authService.save(user));
    }
}