package fezlr.fluffy.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank
        String email,

        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
