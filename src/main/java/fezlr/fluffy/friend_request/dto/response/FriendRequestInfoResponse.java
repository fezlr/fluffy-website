package fezlr.fluffy.friend_request.dto.response;

import fezlr.fluffy.profile.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FriendRequestInfoResponse(
        Long profileId,

        String firstName,

        String lastName,

        LocalDate birthDate,

        Gender gender,

        String mainPhotoUrl,

        LocalDateTime createdAt
) {
}
