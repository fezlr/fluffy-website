package fezlr.fluffy.token.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TokenResponse(
        @NotNull
        Long id,

        @NotBlank
        String token,

        @NotNull
        LocalDateTime createdAt,

        @NotNull
        LocalDateTime expiresAt,

        @NotNull
        LocalDateTime confirmedAt,

        @NotNull
        Long userId
) {
}
