package fezlr.fluffy.settings.controller.page;

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
@RequestMapping("/profile/settings")
public class ProfileSettingsPageController {
    private final CustomAuthService customAuthService;

    @GetMapping
    public String profileSettings(Model model) {
        log.info("Called profileSettings");
        var user = customAuthService.getCurrentUser();
        model.addAttribute("account", user);
        model.addAttribute("profile", user.getProfile());
        return "profile/settings";
    }
}
