package fezlr.fluffy.chat.dto.response;

import java.time.LocalDateTime;

public record ChatResponse(
        String name,

        Long userOneId,

        Long userTwoId,

        LocalDateTime createdAt
) {
}
