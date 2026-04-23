package fezlr.fluffy.friend_request.controller.api;

import fezlr.fluffy.friend_request.dto.request.CreateFriendRequest;
import fezlr.fluffy.friend_request.dto.request.DeleteFriendRequest;
import fezlr.fluffy.friend_request.dto.response.CreateFriendResponse;
import fezlr.fluffy.friend_request.dto.response.DeleteFriendResponse;
import fezlr.fluffy.friend_request.service.FriendRequestService;
import jakarta.validation.Valid;
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

    @PostMapping("/create")
    public ResponseEntity<CreateFriendResponse> createFriendRequest(@Valid @RequestBody CreateFriendRequest request) {
        log.info("Called createFriendRequest with BODY = {}", request);
        return ResponseEntity.ok(friendRequestService.createFriendRequest(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteFriendResponse> deleteFriendRequest(@Valid @RequestBody DeleteFriendRequest request) {
        log.info("Called deleteFriendRequest with BODY = {}", request);
        return ResponseEntity.ok(friendRequestService.deleteFriendRequest(request));
    }
}