package fezlr.fluffy.common.component;

import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserEntity user = resolveUser(authentication);

        if (user.getProfile() == null || !user.getProfile().isComplete()) {
            response.sendRedirect("/profile/setup");
        } else {
            response.sendRedirect("/home");
        }
    }

    //find whether oauth2 or custom login
    private UserEntity resolveUser(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2) {
            String email = oAuth2.getPrincipal().getAttribute("email");
            return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email is not found"));
        } else {
            String username = authentication.getName();
            return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username is not found"));
        }
    }
}