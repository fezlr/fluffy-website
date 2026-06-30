package fezlr.fluffy.mail.property;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("app.mail-messages")
public record MailPropertiesMessages(
        @NotBlank String codeSubjectMessage,
        @NotBlank String linkSubjectMessage,
        @NotBlank String codeMessage,
        @NotBlank String linkMessage,
        @NotBlank String resetEmailMessage
) {
}
