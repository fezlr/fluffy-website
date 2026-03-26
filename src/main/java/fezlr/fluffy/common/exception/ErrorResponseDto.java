package fezlr.fluffy.common.exception;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,

        String detailedMessage,

        LocalDateTime errorLocalDateTime
) {
}
