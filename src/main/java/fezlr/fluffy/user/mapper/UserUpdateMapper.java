package fezlr.fluffy.user.mapper;

import fezlr.fluffy.user.dto.response.UserUpdateResponse;
import fezlr.fluffy.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateMapper {
    public UserUpdateResponse toResponse(UserEntity entity) {
        return new UserUpdateResponse(
                entity.getUsername(),
                entity.getEmail()
        );
    }
}
