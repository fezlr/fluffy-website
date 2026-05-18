package fezlr.fluffy.message.dto.response;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MessageResponse(
        @NotNull
        Long chatId,

        @NotNull
        Long userId,

        @NotNull
        String username,

        String userMainPhotoUrl,

        @NotNull
        String text,

        @NotNull
        LocalDateTime createdAt
) {
}
