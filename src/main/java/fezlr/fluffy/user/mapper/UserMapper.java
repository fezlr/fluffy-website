package fezlr.fluffy.user.mapper;

import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.dto.response.UserResponse;
import fezlr.fluffy.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity toEntity(UserRequest user) {
        return new UserEntity(
                user.id(),
                user.email(),
                user.username(),
                user.password(),
                user.role()
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
