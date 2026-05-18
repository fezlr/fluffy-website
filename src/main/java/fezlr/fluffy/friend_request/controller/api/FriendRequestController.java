package fezlr.fluffy.friend_request.controller.api;

import fezlr.fluffy.friend_request.dto.response.CreateFriendResponse;
import fezlr.fluffy.friend_request.dto.response.DeleteFriendResponse;
import fezlr.fluffy.friend_request.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/friend-request")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/create/{receiverId}")
    public ResponseEntity<CreateFriendResponse> createFriendRequest(@PathVariable("receiverId") Long receiverId, @RequestParam Long senderId) {
        return ResponseEntity.ok(friendRequestService.createFriendRequest(senderId, receiverId));
    }

    @DeleteMapping("/delete/{receiverId}")
    public ResponseEntity<DeleteFriendResponse> deleteFriendRequest(@PathVariable("receiverId") Long receiverId, @RequestParam Long senderId) {
        return ResponseEntity.ok(friendRequestService.deleteFriendRequest(senderId, receiverId));
    }
}