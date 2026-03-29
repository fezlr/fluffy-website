package fezlr.fluffy.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendResetPasswordRequest(
        @NotBlank
        String email
) {
}
