package fezlr.fluffy.profile.mapper;

import fezlr.fluffy.profile.dto.response.ProfilePotentialFriendsResponse;
import fezlr.fluffy.profile.dto.response.ProfileResponse;
import fezlr.fluffy.profile.entity.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfilePotentialFriendsMapper {
    public ProfilePotentialFriendsResponse toResponse(ProfileEntity entity) {
        return new ProfilePotentialFriendsResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthDate(),
                entity.getGender(),
                entity.getCity(),
                entity.getAboutMe(),
                entity.getMainPhotoUrl()
        );
    }
}