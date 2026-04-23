package fezlr.fluffy.profile.controller.page;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.profile.service.ProfileService;
import fezlr.fluffy.user.service.UserService;
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
    private final UserService userService;

    @GetMapping
    public String myProfile() {
        return "redirect:/profiles/" + customAuthService.getCurrentUser().getProfile().getId();
    }

    @GetMapping("/{id}")
    public String findProfile(@PathVariable Long id, Model model) {
        log.info("Called findProfile()");
        var user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("profile", user.getProfile());
        model.addAttribute("isOwnProfile", customAuthService.getCurrentUser().getProfile().getId().equals(id));
        return "profile/profile";
    }
}