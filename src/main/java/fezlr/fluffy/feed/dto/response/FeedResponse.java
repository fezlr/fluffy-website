package fezlr.fluffy.feed.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedResponse(
        @NotNull
        Long id,

        @NotNull
        Long userId,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        String avatarUrl,

        String photoUrl,

        String text
) {
}
