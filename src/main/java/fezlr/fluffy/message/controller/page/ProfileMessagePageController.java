package fezlr.fluffy.message.controller.page;

import fezlr.fluffy.common.service.CustomAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/profile/messages")
public class ProfileMessagePageController {
    private final CustomAuthService customAuthService;

    @GetMapping
    public String profileMessages(Model model) {
        log.info("Called profileMessages");
        model.addAttribute("profile", customAuthService.getCurrentUser().getProfile());
        return "profile/messages";
    }
}