package fezlr.fluffy.message.mapper;

import fezlr.fluffy.message.dto.response.MessageResponse;
import fezlr.fluffy.message.entity.MessageEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public MessageResponse toResponse(MessageEntity entity) {
        return new MessageResponse(
                entity.getChat().getId(),
                entity.getUser().getId(),
                entity.getText(),
                entity.getCreatedAt()
        );
    }
}