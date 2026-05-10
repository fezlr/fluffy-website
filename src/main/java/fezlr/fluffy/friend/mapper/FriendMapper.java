package fezlr.fluffy.friend.mapper;

import fezlr.fluffy.friend.dto.response.FriendResponse;
import fezlr.fluffy.friend.entity.FriendEntity;
import org.springframework.stereotype.Component;

@Component
public class FriendMapper {
    public FriendResponse toResponse(FriendEntity entity) {
        return new FriendResponse(
                entity.getFriend().getProfile().getId(),
                entity.getFriend().getProfile().getFirstName(),
                entity.getFriend().getProfile().getLastName(),
                entity.getFriend().getProfile().getBirthDate(),
                entity.getFriend().getProfile().getGender(),
                entity.getFriend().getProfile().getMainPhotoUrl()
        );
    }
}