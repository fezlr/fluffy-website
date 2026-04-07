package fezlr.fluffy.token.dto.response;

import fezlr.fluffy.token.enums.TokenType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TokenResponse(
        @NotNull
        Long id,

        @NotBlank
        String token,

        @Nullable
        String code,

        @NotNull
        LocalDateTime createdAt,

        @NotNull
        LocalDateTime expiresAt,

        @NotNull
        LocalDateTime confirmedAt,

        @NotNull
        Long userId,

        @NotNull
        TokenType tokenType,

        boolean isActive
) {
}
