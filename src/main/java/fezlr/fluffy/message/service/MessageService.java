package fezlr.fluffy.message.service;

import fezlr.fluffy.chat.service.ChatService;
import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.message.dto.request.MessageRequest;
import fezlr.fluffy.message.dto.response.MessageResponse;
import fezlr.fluffy.message.dto.response.MessageDeleteResponse;
import fezlr.fluffy.message.entity.MessageEntity;
import fezlr.fluffy.message.exception.MessageAccessDeniedException;
import fezlr.fluffy.message.mapper.MessageMapper;
import fezlr.fluffy.message.repository.MessageRepository;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.enums.Role;
import fezlr.fluffy.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final CustomAuthService customAuthService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserService userService;
    private final ChatService chatService;

    @Transactional
    public MessageResponse create(MessageRequest request) {
        if (!chatService.existsById(request.chatId())) {
            throw new IllegalStateException("Chat is not found");
        }

        if (!userService.existsById(request.userId())) {
            throw new IllegalStateException("User is not found");
        }

        MessageEntity entity = MessageEntity
                .builder()
                .user(userService.findById(request.userId()))
                .chat(chatService.findById(request.chatId()))
                .text(request.text())
                .createdAt(LocalDateTime.now())
                .build();

        return messageMapper.toResponse(messageRepository.save(entity));
    }

    @Transactional
    public MessageResponse update(Long id, String text) {
        boolean isChanged = false;
        UserEntity currentUser = customAuthService.getCurrentUser();
        MessageEntity entity = messageRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message is not found"));

        //check if you're in chat
        if (!entity.getUser().getId().equals(currentUser.getId()) && entity.getUser().getRole() != Role.ADMIN) {
            throw new MessageAccessDeniedException("No rights");
        }

        //check if text changed
        if (!Objects.equals(entity.getText(), text)) {
            isChanged = true;
            entity.setText(text);
        }

        //save if changed
        if (isChanged) {
            return messageMapper.toResponse(messageRepository.save(entity));
        }

        //return entity which was before without saving
        return messageMapper.toResponse(entity);
    }

    @Transactional
    public MessageDeleteResponse delete(Long id) {
        messageRepository.deleteById(id);
        return new MessageDeleteResponse(id, "Message has been successfully deleted");
    }

    public List<MessageResponse> getByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId)
                .stream()
                .map(messageMapper::toResponse)
                .toList();
    }
}
