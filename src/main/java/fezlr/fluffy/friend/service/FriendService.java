package fezlr.fluffy.friend.service;

import fezlr.fluffy.friend.dto.response.DeleteResponse;
import fezlr.fluffy.friend.dto.response.FriendResponse;
import fezlr.fluffy.friend.entity.FriendEntity;
import fezlr.fluffy.friend.mapper.FriendMapper;
import fezlr.fluffy.friend.repository.FriendRepository;
import fezlr.fluffy.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
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
        log.info("Create friendship");
        if (friendRepository.existsByUserAndFriend(senderEntity, receiverEntity)) {
            throw new IllegalStateException("Friendship already exists");
        }

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

        log.info("save senderToSave = {}, receiverToSave = {}", senderToSave, receiverToSave);
        friendRepository.save(senderToSave);
        friendRepository.save(receiverToSave);
    }

    @Transactional
    public void delete(UserEntity senderId, UserEntity receiverId) {
        friendRepository.deleteByUserId(senderId.getProfile().getId());
        friendRepository.deleteByFriendId(receiverId.getProfile().getId());
    }

    public boolean existsFriendship(Long senderId, Long receiverId) {
        return friendRepository.existsBySenderIdAndReceiverId(senderId, receiverId) ||
                friendRepository.existsBySenderIdAndReceiverId(receiverId, senderId);
    }

    @Transactional
    public DeleteResponse deleteFriendship(Long senderId, Long receiverId) {
        log.info("Called deleteFriendship with senderId = {}, receiverId = {}", senderId, receiverId);
        if (senderId.equals(receiverId)) {
            throw new IllegalStateException("Cannot delete friendship with yourself");
        }

        friendRepository.deleteByUserId(senderId);
        friendRepository.deleteByUserId(receiverId);

        friendRepository.deleteByFriendId(senderId);
        friendRepository.deleteByFriendId(receiverId);

        return new DeleteResponse("Successfully deleted");
    }
}