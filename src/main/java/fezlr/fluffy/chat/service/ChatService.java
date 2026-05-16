package fezlr.fluffy.chat.service;

import fezlr.fluffy.chat.dto.request.ChatCreateRequest;
import fezlr.fluffy.chat.dto.request.ChatDirectRequest;
import fezlr.fluffy.chat.dto.request.ChatUpdateRequest;
import fezlr.fluffy.chat.dto.response.ChatDeleteResponse;
import fezlr.fluffy.chat.dto.response.ChatResponse;
import fezlr.fluffy.chat.entity.ChatEntity;
import fezlr.fluffy.chat.mapper.ChatMapper;
import fezlr.fluffy.chat.repository.ChatRepository;
import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final CustomAuthService customAuthService;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserService userService;

    public List<ChatResponse> findAllChatsByCurrentUser() {
        Long userId = customAuthService.getCurrentUser().getId();
        return chatRepository
                .findAllByUserId(userId)
                .stream()
                .map(chatMapper::toResponse)
                .toList();
    }

    @Transactional
    public ChatResponse create(ChatCreateRequest chatCreateRequest) {
        UserEntity currentUser = customAuthService.getCurrentUser();

        if (chatCreateRequest.userOneId().equals(currentUser.getId())) {
            throw new IllegalStateException("Cannot chat with yourself");
        }

        ChatEntity entity = ChatEntity.builder()
                .name(chatCreateRequest.name())
                .userOne(userService.findById(chatCreateRequest.userOneId()))
                .userTwo(currentUser)
                .build();

        return chatMapper.toResponse(chatRepository.save(entity));
    }

    @Transactional
    public ChatResponse update(Long id, ChatUpdateRequest request) {
        ChatEntity entity = chatRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chat is not found"));

        Long currentUserId = customAuthService.getCurrentUser().getId();

        if (!entity.getUserOne().getId().equals(currentUserId) && !entity.getUserTwo().getId().equals(currentUserId)) {
            throw new IllegalStateException("Not in chat");
        }

        entity.setName(request.name());
        entity.setPhotoUrl(request.photoUrl());

        return chatMapper.toResponse(chatRepository.save(entity));
    }

    @Transactional
    public ChatDeleteResponse delete(Long id) {
        chatRepository.deleteById(id);
        return new ChatDeleteResponse(id, "Delete has been successfully done");
    }

    public ChatEntity findById(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Chat is not found"));
    }

    public boolean existsById(Long id) {
        return chatRepository.existsById(id);
    }

    public ChatResponse getOrCreateDirectChat(ChatDirectRequest request) {

        log.info("REQUEST = {}", request);
        //get currentUserId
        Long currentUserId = customAuthService.getCurrentUser().getId();

        //validation
        if (currentUserId.equals(request.userId())) {
            throw new IllegalStateException("Cannot chat with yourself");
        }

        //find if chat doesn't exist
        if (!chatRepository.existsMutualByUserOneIdAndUserTwoId(currentUserId, request.userId())) {
            create(new ChatCreateRequest("Group", request.userId()));
        }

        //find if exists
        ChatEntity chat = chatRepository
                .findMutualByUserOneIdAndUserTwoId(currentUserId, request.userId())
                .orElseThrow(() -> new EntityNotFoundException("Chat is not found"));

        return chatMapper.toResponse(chat);
    }
}
