package fezlr.fluffy.friend_request.mapper;

import fezlr.fluffy.friend_request.dto.response.CreateFriendResponse;
import fezlr.fluffy.friend_request.entity.FriendRequestEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FriendRequestMapper {
    public CreateFriendResponse toResponse(FriendRequestEntity friendRequestEntity, LocalDateTime time) {
        return new CreateFriendResponse(
                friendRequestEntity.getSenderId(),
                friendRequestEntity.getReceiverId(),
                time
        );
    }
}
