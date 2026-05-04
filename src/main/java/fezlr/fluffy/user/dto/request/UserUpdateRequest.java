package fezlr.fluffy.user.dto.request;

public record UserUpdateRequest(
        String username,

        String email
) {
}
