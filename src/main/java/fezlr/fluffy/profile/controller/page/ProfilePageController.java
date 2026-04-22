package fezlr.fluffy.profile.controller.page;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.profile.dto.response.ProfileResponse;
import fezlr.fluffy.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/profiles")
public class ProfilePageController {
    private final CustomAuthService customAuthService;
    private final ProfileService profileService;

    @GetMapping
    public String myProfile() {
        return "redirect:/profiles/" + customAuthService.getCurrentUser().getProfile().getId();
    }

    @GetMapping("/{id}")
    public String findProfile(@PathVariable Long id, Model model) {
        log.info("Called findProfile()");
        model.addAttribute("user", customAuthService.getCurrentUser());
        model.addAttribute("profile", profileService.findProfile(id));
        model.addAttribute("isOwnProfile", customAuthService.getCurrentUser().getProfile().getId().equals(id));
        return "profile/profile";
    }
}