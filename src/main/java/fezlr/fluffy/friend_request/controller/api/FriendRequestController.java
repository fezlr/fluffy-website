package fezlr.fluffy.friend_request.controller.api;

import fezlr.fluffy.friend_request.dto.response.CreateFriendResponse;
import fezlr.fluffy.friend_request.dto.response.DeleteFriendResponse;
import fezlr.fluffy.friend_request.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/friend-request")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/create/{receiverId}")
    public ResponseEntity<CreateFriendResponse> createFriendRequest(@PathVariable("receiverId") Long receiverId, @RequestParam Long senderId) {
        log.info("Called createFriendRequest with senderId = {}, receiverId = {}", senderId, receiverId);
        return ResponseEntity.ok(friendRequestService.createFriendRequest(senderId, receiverId));
    }

    @DeleteMapping("/delete/{receiverId}")
    public ResponseEntity<DeleteFriendResponse> deleteFriendRequest(@PathVariable("receiverId") Long receiverId, @RequestParam Long senderId) {
        log.info("Called deleteFriendRequest with senderId = {}, receiverId = {}", senderId, receiverId);
        return ResponseEntity.ok(friendRequestService.deleteFriendRequest(senderId, receiverId));
    }
}