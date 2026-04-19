package fezlr.fluffy.profile.dto.response;

import fezlr.fluffy.profile.enums.Gender;
import java.time.LocalDate;

public record ProfileResponse(
        String firstName,

        String lastName,

        LocalDate birthDate,

        Gender gender,

        String city,

        String aboutMe,

        String mainPhotoUrl
) {
}
