package fezlr.fluffy.friend_request.dto.request;

import fezlr.fluffy.profile.dto.request.ProfileRequest;
import fezlr.fluffy.profile.entity.ProfileEntity;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateFriendRequest(
        @NotBlank
        Long senderId,

        @NotBlank
        Long receiverId
) {
}
