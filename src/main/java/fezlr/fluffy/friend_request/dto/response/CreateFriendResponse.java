package fezlr.fluffy.friend_request.dto.response;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateFriendResponse(
        @NotBlank
        Long senderId,

        @NotBlank
        Long receiverId,

        @NotBlank
        LocalDateTime createdAt
) {
}