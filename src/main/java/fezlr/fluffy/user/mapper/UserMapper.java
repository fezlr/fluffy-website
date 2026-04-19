package fezlr.fluffy.user.mapper;

import fezlr.fluffy.auth.enums.Provider;
import fezlr.fluffy.profile.entity.ProfileEntity;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.dto.response.UserResponse;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    //TODO: builder
    public UserEntity toEntity(UserRequest user, ProfileEntity profileEntity, Role role, Provider provider, boolean isEnabled) {
        return new UserEntity(
                null,
                profileEntity,
                user.email(),
                user.username(),
                user.password(),
                role,
                provider,
                isEnabled
        );
    }

    public UserResponse toResponse(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getUsername(),
                userEntity.getRole(),
                userEntity.getProvider(),
                userEntity.isEnabled()
        );
    }
}
