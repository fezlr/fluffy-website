package fezlr.fluffy.friend_request;

import fezlr.fluffy.friend.service.FriendService;
import fezlr.fluffy.friend_request.dto.response.CreateFriendResponse;
import fezlr.fluffy.friend_request.dto.response.DeleteFriendResponse;
import fezlr.fluffy.friend_request.entity.FriendRequestEntity;
import fezlr.fluffy.friend_request.mapper.FriendRequestMapper;
import fezlr.fluffy.friend_request.repository.FriendRequestRepository;
import fezlr.fluffy.friend_request.service.FriendRequestService;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FriendRequestServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendRequestRepository friendRequestRepository;
    @Mock
    private FriendRequestMapper friendRequestMapper;
    @Mock
    private FriendService friendService;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @Test
    void createFriendRequest_shouldThrow_whenSenderEqualsReceiver() {
        assertThrows(IllegalArgumentException.class,
                () -> friendRequestService.createFriendRequest(1L, 1L));
    }

    @Test
    void createFriendRequest_shouldCreateRequest_whenNoExisting() {
        Long senderId = 1L;
        Long receiverId = 2L;

        FriendRequestEntity entity = FriendRequestEntity.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .createdAt(LocalDateTime.now())
                .build();

        when(friendRequestRepository.existsBySenderIdAndReceiverId(receiverId, senderId))
                .thenReturn(false);

        when(friendRequestRepository.existsBySenderIdAndReceiverId(senderId, receiverId))
                .thenReturn(false);

        when(friendRequestRepository.save(any()))
                .thenReturn(entity);

        when(friendRequestMapper.toResponse(any(), any()))
                .thenReturn(new CreateFriendResponse(senderId, receiverId, LocalDateTime.now()));

        CreateFriendResponse response =
                friendRequestService.createFriendRequest(senderId, receiverId);

        assertEquals(senderId, response.senderId());
        assertEquals(receiverId, response.receiverId());
    }

    @Test
    void createFriendRequest_shouldCreateFriendship_whenMutualRequestExists() {
        Long senderId = 1L;
        Long receiverId = 2L;

        UserEntity sender = UserEntity.builder().id(senderId).build();
        UserEntity receiver = UserEntity.builder().id(receiverId).build();

        when(friendRequestRepository.existsBySenderIdAndReceiverId(receiverId, senderId))
                .thenReturn(true);

        when(userRepository.findById(senderId))
                .thenReturn(Optional.of(sender));

        when(userRepository.findById(receiverId))
                .thenReturn(Optional.of(receiver));

        when(friendRequestMapper.toResponse(any(), any()))
                .thenReturn(new CreateFriendResponse(senderId, receiverId, LocalDateTime.now()));

        friendRequestService.createFriendRequest(senderId, receiverId);

        verify(friendRequestRepository)
                .deleteBySenderIdAndReceiverId(receiverId, senderId);

        verify(friendService)
                .createFriendship(sender, receiver);
    }

    @Test
    void deleteFriendRequest_shouldDeleteAndCallFriendService() {
        Long senderId = 1L;
        Long receiverId = 2L;

        FriendRequestEntity entity = FriendRequestEntity.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        when(friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId))
                .thenReturn(Optional.of(entity));

        UserEntity sender = UserEntity.builder().id(senderId).build();
        UserEntity receiver = UserEntity.builder().id(receiverId).build();

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        DeleteFriendResponse response =
                friendRequestService.deleteFriendRequest(senderId, receiverId);

        verify(friendRequestRepository).delete(entity);
        verify(friendService).delete(sender, receiver);
        assertEquals(senderId, response.senderId());
        assertEquals(receiverId, response.receiverId());
    }

    @Test
    void exists_shouldReturnTrue() {
        when(friendRequestRepository.existsBySenderIdAndReceiverId(1L, 2L))
                .thenReturn(true);

        assertTrue(friendRequestService.existsBySenderIdAndReceiverId(1L, 2L));
    }
}