package fezlr.fluffy.auth.oauth2.service;

import fezlr.fluffy.auth.enums.Provider;
import fezlr.fluffy.profile.entity.ProfileEntity;
import fezlr.fluffy.profile.service.ProfileService;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.enums.Role;
import fezlr.fluffy.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OAuthService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = Optional.ofNullable(oAuth2User.<String>getAttribute("email"))
                .orElseThrow(() -> new OAuth2AuthenticationException("Email not provided by Google"));

        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        existing -> {
                            if (existing.getProvider() != Provider.GOOGLE) {
                                throw new OAuth2AuthenticationException("Account is registered via " + existing.getProvider());
                            }
                        },
                        () -> registerUserAndProfile(email, oAuth2User.getAttribute("name"))
                );

        return oAuth2User;
    }

    @Transactional
    private void registerUserAndProfile(String email, String name) {
        var user = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .username(name)
                .provider(Provider.GOOGLE)
                .role(Role.USER)
                .isEnabled(true)
                .build();

        ProfileEntity profile = profileService.create(user);
        user.setProfile(profile);

        userRepository.save(user);
    }
}
