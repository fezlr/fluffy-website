package fezlr.fluffy.auth.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.auth.messages")
public record AuthPropertiesMessages(
        @NotBlank String codeSent,
        @NotBlank String codeConfirmed,
        @NotBlank String resetSent,
        @NotBlank String resetDone,
        @NotBlank String resetAllowed,
        @NotBlank String resetEmail,
        @NotBlank String allowed
) {
}
