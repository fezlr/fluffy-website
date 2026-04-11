package fezlr.fluffy.auth.controller.page;

import fezlr.fluffy.auth.dto.response.AuthResponse;
import fezlr.fluffy.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/reset-password")
public class ResetPasswordController {
    private final AuthService authService;

//    @GetMapping
//    public String resetPasswordPage() {
//        return "reset-password";
//    }

    //when entering the page and getting token
    @GetMapping
    public String validateResetPasswordToken(@RequestParam String token) {
        log.info("Called validateResetPasswordToken with BODY = {}", token);
        authService.validateResetPasswordToken(token);
        return "reset-password";
    }
}