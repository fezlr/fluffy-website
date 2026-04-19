package fezlr.fluffy.profile.mapper;

import fezlr.fluffy.profile.dto.response.ProfileResponse;
import fezlr.fluffy.profile.entity.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {
    public ProfileResponse toResponse(ProfileEntity entity) {
        return new ProfileResponse(
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