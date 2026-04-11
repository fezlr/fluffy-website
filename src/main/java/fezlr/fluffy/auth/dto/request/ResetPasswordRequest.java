package fezlr.fluffy.auth.dto.request;

import fezlr.fluffy.common.constant.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank
        String token,

        @NotBlank
        @Size(min = ValidationConstants.PASSWORD_SIZE_MIN, max = ValidationConstants.PASSWORD_SIZE_MAX)
        String newPassword,

        @NotBlank
        @Size(min = ValidationConstants.PASSWORD_SIZE_MIN, max = ValidationConstants.PASSWORD_SIZE_MAX)
        String confirmPassword
){
}
