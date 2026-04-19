package fezlr.fluffy.profile.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileSetupPageController {
    @GetMapping("/setup")
    public String profileSetupPage() {
        return "profile/setup";
    }
}