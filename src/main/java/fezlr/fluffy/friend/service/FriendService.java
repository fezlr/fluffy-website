package fezlr.fluffy.friend.service;

import fezlr.fluffy.friend.dto.response.FriendResponse;
import fezlr.fluffy.friend.entity.FriendEntity;
import fezlr.fluffy.friend.mapper.FriendMapper;
import fezlr.fluffy.friend.repository.FriendRepository;
import fezlr.fluffy.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;

    public Page<FriendResponse> findFriends(Long userId, Pageable pageable) {
        log.info("Called findFriends with ID = {}, PAGE = {}", userId, pageable);
        return friendRepository
                .findFriends(userId, pageable)
                .map(friendMapper::toResponse);
    }

    @Transactional
    public void createFriendship(UserEntity senderEntity, UserEntity receiverEntity) {
        LocalDateTime time = LocalDateTime.now();
        FriendEntity senderToSave = FriendEntity.builder()
                .user(senderEntity)
                .friend(receiverEntity)
                .createdAt(time)
                .build();

        FriendEntity receiverToSave = FriendEntity.builder()
                .user(receiverEntity)
                .friend(senderEntity)
                .createdAt(time)
                .build();

        friendRepository.save(senderToSave);
        friendRepository.save(receiverToSave);
    }

    @Transactional
    public void delete(UserEntity senderId, UserEntity receiverId) {
        friendRepository.deleteByUser(senderId.getProfile().getId());
        friendRepository.deleteByFriend(receiverId.getProfile().getId());
    }
}