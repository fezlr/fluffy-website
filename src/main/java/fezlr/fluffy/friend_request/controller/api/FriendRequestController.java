package fezlr.fluffy.friend_request.controller.api;

import fezlr.fluffy.friend_request.dto.request.CreateFriendRequest;
import fezlr.fluffy.friend_request.dto.response.CreateFriendResponse;
import fezlr.fluffy.friend_request.service.FriendRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/friend-request")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/create")
    public ResponseEntity<CreateFriendResponse> createFriendRequest(@Valid @RequestBody CreateFriendRequest request) {
        log.info("Called createFriendRequest");
        return ResponseEntity.ok(friendRequestService.createFriendRequest(request));
    }

}