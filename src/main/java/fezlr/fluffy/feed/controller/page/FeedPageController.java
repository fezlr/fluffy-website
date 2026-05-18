package fezlr.fluffy.feed.controller.page;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.feed.constant.FeedConstants;
import fezlr.fluffy.feed.dto.response.FeedResponse;
import fezlr.fluffy.feed.service.FeedService;
import fezlr.fluffy.profile.dto.response.ProfilePotentialFriendsResponse;
import fezlr.fluffy.profile.dto.response.ProfileResponse;
import fezlr.fluffy.profile.service.ProfileService;
import fezlr.fluffy.user.entity.UserEntity;
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
@RequestMapping("/home")
public class FeedPageController {
    private final CustomAuthService customAuthService;
    private final ProfileService profileService;

    @GetMapping
    public String feedPage(@PageableDefault(size = 20) Pageable pageable, Model model) {
        UserEntity user = customAuthService.getCurrentUser();
        Page<ProfilePotentialFriendsResponse> potentialFriends = profileService.findPotentialFriends(user.getProfile().getId(), pageable);

        model.addAttribute("potentialFriends", potentialFriends);
        model.addAttribute("feed", Math.random());
        model.addAttribute("feedSize", FeedConstants.SIZE);
        model.addAttribute("user", user);

        return "feed/home";
    }
}