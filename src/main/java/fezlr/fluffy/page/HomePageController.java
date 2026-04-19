package fezlr.fluffy.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/home")
public class HomePageController {
    @GetMapping
    public String homeController() {
        log.info("Called homeController()");
        return "page/home";
    }
}
