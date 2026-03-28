package fezlr.fluffy.user.mapper;

import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.dto.response.UserResponse;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    //TODO: builder
    public UserEntity toEntity(UserRequest user, Role role) {
        return new UserEntity(
                null,
                user.email(),
                user.username(),
                user.password(),
                role
        );
    }

    public UserResponse toResponse(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getUsername(),
                userEntity.getRole()
        );
    }
}
