package fezlr.fluffy.user.dto.request;

import fezlr.fluffy.common.constant.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank
        String email,

        @NotBlank
        String username,

        @Size(min = ValidationConstants.PASSWORD_SIZE_MIN, max = ValidationConstants.PASSWORD_SIZE_MAX, message = "Password must be between 8 and 64 characters")
        @NotBlank
        String password
) {
}
