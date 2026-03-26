package fezlr.fluffy.user.dto.response;

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
        Role role
) {
}
