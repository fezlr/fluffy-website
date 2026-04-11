package fezlr.fluffy.auth.controller.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/reset-password-complete")
public class MessageResetPasswordController {
    @GetMapping
    public String messageResetPasswordPage() {
        return "reset-password-complete";
    }
}
