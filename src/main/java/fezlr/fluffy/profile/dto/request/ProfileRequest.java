package fezlr.fluffy.profile.dto.request;

import fezlr.fluffy.common.constant.ValidationConstants;
import fezlr.fluffy.profile.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProfileRequest(
        @Size(min = ValidationConstants.FIRST_NAME_SIZE_MIN, max = ValidationConstants.FIRST_NAME_SIZE_MAX)
        @NotBlank
        String firstName,

        @Size(min = ValidationConstants.LAST_NAME_SIZE_MIN, max = ValidationConstants.LAST_NAME_SIZE_MAX)
        @NotBlank
        String lastName,

        LocalDate birthDate,

        Gender gender,

        String city,

        String aboutMe,

        String mainPhotoUrl
) {
}