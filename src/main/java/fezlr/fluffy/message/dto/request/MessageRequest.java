package fezlr.fluffy.message.dto.request;

import jakarta.validation.constraints.NotNull;

public record MessageRequest(
        @NotNull
        Long chatId,

        @NotNull
        Long userId,

        @NotNull
        String text
) {
}
