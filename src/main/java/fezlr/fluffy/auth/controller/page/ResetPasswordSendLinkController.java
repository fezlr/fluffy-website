package fezlr.fluffy.auth.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResetPasswordSendLinkController {
    @GetMapping("/reset-password-send-link")
    public String resetPasswordSendLinkPage() {
        return "auth/reset-password-send-link";
    }
}