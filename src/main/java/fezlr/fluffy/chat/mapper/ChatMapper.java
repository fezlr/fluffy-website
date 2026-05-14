package fezlr.fluffy.chat.mapper;

import fezlr.fluffy.chat.dto.response.ChatResponse;
import fezlr.fluffy.chat.entity.ChatEntity;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {
    public ChatResponse toResponse(ChatEntity entity) {
        return new ChatResponse(
                entity.getName(),
                entity.getUserOne().getId(),
                entity.getUserTwo().getId(),
                entity.getCreatedAt()
        );
    }
}
