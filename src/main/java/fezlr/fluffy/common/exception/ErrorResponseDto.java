package fezlr.fluffy.common.exception;

public record ErrorResponseDto(
        int status,

        String error,

        String message,

        String timestamp
) {
}
