package fezlr.fluffy.friend.controller.api;

import fezlr.fluffy.friend.dto.response.DeleteResponse;
import fezlr.fluffy.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/friend")
public class FriendController {
    private final FriendService friendService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> deleteFriendship(@PathVariable("id") Long receiverId, @RequestParam Long senderId) {
        log.info("Called deleteFriendship with receiverId = {}, senderId = {}", receiverId, senderId);
        return ResponseEntity.ok(friendService.deleteFriendship(senderId, receiverId));
    }

}
