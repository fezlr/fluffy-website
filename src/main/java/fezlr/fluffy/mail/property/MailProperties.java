package fezlr.fluffy.mail.property;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("spring.mail")
public record MailProperties(
        @NotBlank String name,
        @NotBlank String username
) {
}
