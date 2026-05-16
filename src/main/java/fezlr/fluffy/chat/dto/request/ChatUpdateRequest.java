package fezlr.fluffy.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatUpdateRequest(
        @NotBlank
        String name,

        String photoUrl
) {
}
