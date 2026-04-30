package fezlr.fluffy.friend.controller.page;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.friend.dto.response.FriendResponse;
import fezlr.fluffy.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/profile/friends")
public class FriendPageController {
    private final CustomAuthService customAuthService;
    private final FriendService friendService;

    @GetMapping
    public String friends(@PageableDefault(size = 20) Pageable pageable, Model model) {
        Long userId = customAuthService.getCurrentUser().getProfile().getId();

        Page<FriendResponse> friends = friendService.findFriends(userId, pageable);

        if(friends.getTotalElements() == 0 || friends.isEmpty()) {
            return "redirect:/profile/friends/not-found";
        }

        model.addAttribute("friends", friends);
        return "profile/friends";
    }

    @GetMapping("/not-found")
    public String friendsNotFound() {
        return "profile/friends-not-found";
    }
}