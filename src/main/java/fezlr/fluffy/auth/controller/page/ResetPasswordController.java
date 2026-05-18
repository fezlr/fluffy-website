package fezlr.fluffy.auth.controller.page;

import fezlr.fluffy.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ResetPasswordController {
    private final AuthService authService;

    //when entering the page and getting token
    @GetMapping("/reset-password")
    public String validateResetPasswordToken(@RequestParam String token) {
        authService.validateResetPasswordToken(token);
        return "auth/reset-password";
    }
}