package fezlr.fluffy.friend_request.mapper;

import fezlr.fluffy.friend_request.dto.response.FriendRequestInfoResponse;
import fezlr.fluffy.profile.entity.ProfileEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
  Long profileId,

        String firstName,

        String lastName,

        LocalDate birthDate,

        Gender gender,

        String mainPhotoUrl,

        LocalDateTime createdAt
 */

@Component
public class FriendRequestInfoMapper {
    public FriendRequestInfoResponse toResponse(ProfileEntity entity, LocalDateTime time) {
        return new FriendRequestInfoResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthDate(),
                entity.getGender(),
                entity.getMainPhotoUrl(),
                time
        );
    }
}
