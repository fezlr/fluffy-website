package fezlr.fluffy.friend_request.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeleteFriendRequest(
        @NotBlank
        Long senderId,

        @NotBlank
        Long receiverId
) {
}
