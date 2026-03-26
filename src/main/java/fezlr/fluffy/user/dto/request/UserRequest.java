package fezlr.fluffy.user.dto.request;

import fezlr.fluffy.user.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank
        Long id,

        @NotBlank
        String email,

        @NotBlank
        String username,

        @NotBlank
        String password,

        @NotBlank
        Role role
) {
}
