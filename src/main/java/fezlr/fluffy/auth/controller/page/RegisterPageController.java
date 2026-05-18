package fezlr.fluffy.auth.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterPageController {
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }
}
