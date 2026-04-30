package fezlr.fluffy.feed.controller.page;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.feed.constant.FeedConstants;
import fezlr.fluffy.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping
    public String feedPage(Model model) {
        model.addAttribute("feed", Math.random());
        model.addAttribute("feedSize", FeedConstants.SIZE);
        model.addAttribute("user", customAuthService.getCurrentUser());
        return "feed/home";
    }
}