package fezlr.fluffy.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatCreateRequest(
        @NotBlank
        String name,

        @NotBlank
        Long userOneId
) {
}
