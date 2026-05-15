package fezlr.fluffy.chat.controller.page;

import fezlr.fluffy.chat.controller.api.ChatApiController;
import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile/chats")
public class ChatPageController {
    private final CustomAuthService customAuthService;
    private final ChatApiController chatApiController;

    @GetMapping
    public String profileChats(Model model) {

        model.addAttribute("profile", customAuthService.getCurrentUser().getProfile());
        model.addAttribute("chats", chatApiController.allChatsByCurrentUser());

        return "profile/chat";
    }
}
