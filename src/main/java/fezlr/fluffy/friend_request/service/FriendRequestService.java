package fezlr.fluffy.friend_request.service;

import fezlr.fluffy.friend.service.FriendService;
import fezlr.fluffy.friend_request.dto.response.CreateFriendResponse;
import fezlr.fluffy.friend_request.dto.response.DeleteFriendResponse;
import fezlr.fluffy.friend_request.dto.response.FriendRequestInfoResponse;
import fezlr.fluffy.friend_request.entity.FriendRequestEntity;
import fezlr.fluffy.friend_request.mapper.FriendRequestInfoMapper;
import fezlr.fluffy.friend_request.mapper.FriendRequestMapper;
import fezlr.fluffy.friend_request.repository.FriendRequestRepository;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.repository.UserRepository;
import fezlr.fluffy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendRequestService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRequestMapper friendRequestMapper;
    private final FriendRequestInfoMapper friendRequestInfoMapper;
    private final FriendService friendService;

    @Transactional
    public CreateFriendResponse createFriendRequest(Long senderId, Long receiverId) {
        log.info("Called createFriendRequest with SENDERID = {}, RECEIVERID = {}", senderId, receiverId);
        if(receiverId.equals(senderId)) {
            throw new IllegalArgumentException("Cannot send a friend request to yourself");
        }

        LocalDateTime time = LocalDateTime.now();
        boolean mutualExists = friendRequestRepository.existsBySenderIdAndReceiverId(receiverId, senderId);

        if(mutualExists) {
            log.info("mutualExists");
            var senderIdToSave = userRepository.findById(senderId).orElseThrow(() -> new IllegalArgumentException("Sender user is not found"));
            var receiverIdToSave = userRepository.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("Receiver user is not found"));

            friendRequestRepository.deleteBySenderIdAndReceiverId(receiverId, senderId);
            friendService.createFriendship(senderIdToSave, receiverIdToSave);

            return friendRequestMapper.toResponse(new FriendRequestEntity(), time);
        }

        boolean exists = friendRequestRepository.existsBySenderIdAndReceiverId(senderId, receiverId);

        if(exists) {
            throw new IllegalStateException("A friend request already exists");
        }

        FriendRequestEntity entity = FriendRequestEntity.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .createdAt(time)
                .build();

        return friendRequestMapper.toResponse(friendRequestRepository.save(entity), time);
    }

    @Transactional
    public DeleteFriendResponse deleteFriendRequest(Long senderId, Long receiverId) {
        if(senderId.equals(receiverId)) {
            throw new IllegalStateException("Cannot delete yourself");
        }

        var friendRequestEntity = friendRequestRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
                .orElseThrow(() -> new IllegalStateException("Request is not found"));

        friendRequestRepository.delete(friendRequestEntity);

        var senderIdToSave = userRepository.findById(senderId).orElseThrow(() -> new IllegalArgumentException("Sender user is not found"));
        var receiverIdToSave = userRepository.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("Receiver user is not found"));

        log.info("deleting user with senderId = {}, receiverId = {}", senderId, receiverId);
        friendService.delete(senderIdToSave, receiverIdToSave);

        return new DeleteFriendResponse(
                senderId,
                receiverId,
                LocalDateTime.now());
    }

    public boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId) {
        return friendRequestRepository.existsBySenderIdAndReceiverId(senderId, receiverId);
    }

    public Page<FriendRequestInfoResponse> findOutgoing(Long userId, Pageable pageable) {
        return friendRequestRepository.findAllBySenderId(userId, pageable)
                .map(e -> friendRequestInfoMapper.toResponse(userService.findById(e.getReceiverId()).getProfile(), e.getCreatedAt()));
    }

    public Page<FriendRequestInfoResponse> findIncoming(Long userId, Pageable pageable) {
        return friendRequestRepository.findAllByReceiverId(userId, pageable)
                .map(e -> friendRequestInfoMapper.toResponse(userService.findById(e.getSenderId()).getProfile(), e.getCreatedAt()));
    }
}