package fezlr.fluffy.friend_request.service;

import fezlr.fluffy.friend.service.FriendService;
import fezlr.fluffy.friend_request.dto.request.CreateFriendRequest;
import fezlr.fluffy.friend_request.dto.response.CreateFriendResponse;
import fezlr.fluffy.friend_request.entity.FriendRequestEntity;
import fezlr.fluffy.friend_request.mapper.FriendRequestMapper;
import fezlr.fluffy.friend_request.repository.FriendRequestRepository;
import fezlr.fluffy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class FriendRequestService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRequestMapper friendRequestMapper;
    private final FriendService friendService;

    @Transactional
    public CreateFriendResponse createFriendRequest(CreateFriendRequest request) {
        if(request.receiverId().equals(request.senderId())) {
            throw new IllegalArgumentException("Cannot send a friend request to yourself");
        }

        LocalDateTime time = LocalDateTime.now();
        boolean mutualExists = friendRequestRepository.existsBySenderIdAndReceiverId(request.receiverId(), request.senderId());

        if(mutualExists) {
            var senderIdToSave = userRepository.findById(request.senderId()).orElseThrow(() -> new IllegalArgumentException("Sender user is not found"));
            var receiverIdToSave = userRepository.findById(request.receiverId()).orElseThrow(() -> new IllegalArgumentException("Receiver user is not found"));

            friendService.createFriendship(senderIdToSave, receiverIdToSave);

            return friendRequestMapper.toResponse(new FriendRequestEntity(), time);
        }

        boolean exists = friendRequestRepository.existsBySenderIdAndReceiverId(request.senderId(), request.receiverId());

        if(exists) {
            throw new IllegalStateException("A friend request already exists");
        }

        FriendRequestEntity entity = FriendRequestEntity.builder()
                .senderId(request.senderId())
                .receiverId(request.receiverId())
                .createdAt(time)
                .build();

        return friendRequestMapper.toResponse(friendRequestRepository.save(entity), time);
    }
}