package fezlr.fluffy.auth.dto.request;

import fezlr.fluffy.common.constant.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        String email,

        @NotBlank
        @Size(max = ValidationConstants.USERNAME_SIZE_MAX)
        String username,

        @NotBlank
        @Size(min = ValidationConstants.PASSWORD_SIZE_MIN, max = ValidationConstants.PASSWORD_SIZE_MAX)
        String password,

        @NotBlank
        @Size(min = ValidationConstants.PASSWORD_SIZE_MIN, max = ValidationConstants.PASSWORD_SIZE_MAX)
        String confirmPassword
) {
}
