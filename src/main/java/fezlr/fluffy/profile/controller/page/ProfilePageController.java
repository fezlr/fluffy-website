package fezlr.fluffy.profile.controller.page;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.friend.service.FriendService;
import fezlr.fluffy.friend_request.service.FriendRequestService;
import fezlr.fluffy.user.entity.UserEntity;
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
    private final FriendRequestService friendRequestService;
    private final FriendService friendService;
    private final UserService userService;

    @GetMapping
    public String myProfile() {
        return "redirect:/profiles/" + customAuthService.getCurrentUser().getProfile().getId();
    }

    @GetMapping("/{id}")
    public String findProfile(@PathVariable Long id, Model model) {
        log.info("Called findProfile()");
        var user = userService.findById(id);
        var currentUser = customAuthService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profile", user.getProfile());
        model.addAttribute("isOwnProfile", currentUser.getProfile().getId().equals(id));
        model.addAttribute("hasPendingRequest", friendRequestService.existsBySenderIdAndReceiverId(currentUser.getId(), user.getId()));
        model.addAttribute("isFriend", friendService.existsFriendship(currentUser.getId(), user.getId()));
        return "profile/profile";
    }


}