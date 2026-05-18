package fezlr.fluffy.auth.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageResetPasswordController {
    @GetMapping("/reset-password-complete")
    public String messageResetPasswordPage() {
        return "auth/reset-password-complete";
    }
}
