package fezlr.fluffy.auth.controller.page;

import fezlr.fluffy.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class EmailVerificationPageController {
    private final AuthService authService;

    //when entering the page and getting token
    @GetMapping("/confirm-email")
    public String validateEmailVerificationToken(@RequestParam String token) {
        log.info("Called validateEmailVerificationToken with BODY = {}", token);
        authService.validateEmailVerificationToken(token);
        return "auth/confirm-email";
    }
}
