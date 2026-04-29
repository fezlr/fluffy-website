package fezlr.fluffy.profile.config;

import fezlr.fluffy.profile.component.ProfileSetupInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class ProfileConfig implements WebMvcConfigurer {
    private final ProfileSetupInterceptor profileSetupInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(profileSetupInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/**",
                        "/error",
                        "/favicon.ico",
                        "/profile/setup",
                        "/oauth2/**",
                        "/login/oauth2/**",
                        "/css/**",
                        "/img/**",
                        "/images/**",
                        "/js/**",
                        "/static/favicon.ico",
                        "/api/v1/auth/**",
                        "/login/**",
                        "/register/**",
                        "/reset-password/**",
                        "/reset-password-send-link/**",
                        "/reset-password-complete/**",
                        "/profile/setup/**",
                        "/api/v1/profile/edit/**"
                );
    }
}