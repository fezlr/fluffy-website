package fezlr.fluffy.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatUpdateRequest(
        @NotBlank
        String name,

        String photoUrl
) {
}
