package fezlr.fluffy.chat.dto.response;

import java.time.LocalDateTime;

public record ChatResponse(
        Long id,

        String name,

        String photoUrl,

        Long userOneId,

        Long userTwoId,

        LocalDateTime createdAt
) {
}
