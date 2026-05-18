package fezlr.fluffy.friend.controller.page;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.friend.dto.response.FriendResponse;
import fezlr.fluffy.friend.service.FriendService;
import fezlr.fluffy.friend_request.dto.response.FriendRequestInfoResponse;
import fezlr.fluffy.friend_request.service.FriendRequestService;
import fezlr.fluffy.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile/friends")
public class FriendPageController {
    private final CustomAuthService customAuthService;
    private final FriendRequestService friendRequestService;
    private final FriendService friendService;

    @GetMapping
    public String friends(@PageableDefault(size = 20) Pageable pageable, Model model) {
        UserEntity currentUser = customAuthService.getCurrentUser();
        Long userId = currentUser.getId();
        Long profileId = currentUser.getProfile().getId();

        Page<FriendResponse> friends = friendService.findFriends(userId, pageable);
        Page<FriendRequestInfoResponse> outgoing = friendRequestService.findOutgoing(userId,pageable);
        Page<FriendRequestInfoResponse> incoming = friendRequestService.findIncoming(userId, pageable);

        model.addAttribute("outgoing", outgoing);
        model.addAttribute("incoming", incoming);
        model.addAttribute("userId", profileId);
        model.addAttribute("friends", friends);

        return "profile/friends";
    }
}