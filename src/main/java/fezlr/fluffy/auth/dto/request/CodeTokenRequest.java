package fezlr.fluffy.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CodeTokenRequest(
        @NotBlank
        String email,

        @NotBlank
        String code
) {
}
