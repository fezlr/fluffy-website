package fezlr.fluffy.profile.component;

import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProfileSetupInterceptor implements HandlerInterceptor {
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       log.info("Called preHandle()");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return true;
        }

        UserEntity user = resolveUser(auth);

        if (user.getProfile() == null || !user.getProfile().isComplete()) {
            response.sendRedirect("/profile/setup");
            return false;
        }

        return true;
    }

    private UserEntity resolveUser(Authentication auth) {
        if (auth instanceof OAuth2AuthenticationToken oAuth2) {
            String email = oAuth2.getPrincipal().getAttribute("email");
            return userRepository.findByEmail(email).orElseThrow();
        }
        return userRepository.findByUsername(auth.getName()).orElseThrow();
    }
}