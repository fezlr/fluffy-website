package fezlr.fluffy.auth.dto.request;

import fezlr.fluffy.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//TODO: instead of UserRequest in AuthController save?
public record RegisterRequest(
        @NotBlank
        String email,

        @NotBlank
        @Size(max = ValidationConstants.USERNAME_SIZE_MAX)
        String username,

        @NotBlank
        @Size(min = ValidationConstants.PASSWORD_SIZE_MIN, max = ValidationConstants.PASSWORD_SIZE_MAX)
        String newPassword,

        @NotBlank
        @Size(min = ValidationConstants.PASSWORD_SIZE_MIN, max = ValidationConstants.PASSWORD_SIZE_MAX)
        String confirmPassword
) {
}
