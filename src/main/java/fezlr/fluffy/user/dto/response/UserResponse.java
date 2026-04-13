package fezlr.fluffy.user.dto.response;

import fezlr.fluffy.auth.enums.Provider;
import fezlr.fluffy.user.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record UserResponse(
        @NotBlank
        Long id,

        @NotBlank
        String email,

        @NotBlank
        String username,

        @NotBlank
        Role role,

        @NotBlank
        Provider provider,

        @NotBlank
        boolean isEnabled
) {
}
