package fezlr.fluffy.feed.mapper;

import fezlr.fluffy.feed.dto.response.FeedResponse;
import fezlr.fluffy.feed.entity.FeedEntity;
import org.springframework.stereotype.Component;

@Component
public class FeedMapper {
    public FeedResponse toResponse(FeedEntity entity) {
        return new FeedResponse(
                entity.getUser().getId(),
                entity.getUser().getProfile().getFirstName(),
                entity.getUser().getProfile().getLastName(),
                entity.getUser().getProfile().getMainPhotoUrl(),
                entity.getPhotoUrl(),
                entity.getText()
        );
    }
}
