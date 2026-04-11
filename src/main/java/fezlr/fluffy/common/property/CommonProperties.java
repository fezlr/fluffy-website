package fezlr.fluffy.common.property;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("spring.application")
public record CommonProperties(
        @NotBlank String name,
        @NotBlank String baseURL
) {
}
