package fezlr.fluffy.common.service;

import fezlr.fluffy.auth.security.UserDetailsImpl;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomAuthService {
    private final UserRepository userRepository;

    public UserEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("User is not authenticated");
        }

        if (auth.getPrincipal() instanceof UserDetailsImpl details) {
            return details.getUser();
        }

        if (auth instanceof OAuth2AuthenticationToken oAuth2) {
            String email = oAuth2.getPrincipal().getAttribute("email");
            return userRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Email is not found"));
        }

        throw new IllegalStateException("Unsupported principal type");
    }
}
