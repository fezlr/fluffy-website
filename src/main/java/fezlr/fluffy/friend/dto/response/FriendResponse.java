package fezlr.fluffy.friend.dto.response;

import fezlr.fluffy.profile.enums.Gender;

public record FriendResponse(
        Long profileId,

        String firstName,

        String lastName,

        Gender gender,

        String mainPhotoUrl
) {
}