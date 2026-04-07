package fezlr.fluffy.auth.dto.response;

import jakarta.validation.constraints.NotBlank;

public record RegisterResponse(
        @NotBlank
        String token
) {
}
