package fezlr.fluffy.profile.controller.page;

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
@RequestMapping("/profile/friends")
public class ProfileFriendsPageController {
    private final CustomAuthService customAuthService;

    @GetMapping
    public String profileFriends(Model model) {
        log.info("Called profileFriends");
        model.addAttribute("profile", customAuthService.getCurrentUser().getProfile());
        return "profile/friends";
    }
}
