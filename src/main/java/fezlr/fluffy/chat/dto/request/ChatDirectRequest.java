package fezlr.fluffy.chat.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChatDirectRequest(
        @NotNull
        Long userId
) {
}
