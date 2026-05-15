package fezlr.fluffy.chat.controller.api;

import fezlr.fluffy.chat.dto.request.ChatCreateRequest;
import fezlr.fluffy.chat.dto.response.ChatDeleteResponse;
import fezlr.fluffy.chat.dto.response.ChatResponse;
import fezlr.fluffy.chat.service.ChatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/v1/chats")
public class ChatApiController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatResponse>> allChatsByCurrentUser() {
        return ResponseEntity.ok(chatService.findAllChatsByCurrentUser());
    }

    @PostMapping("/create")
    public ResponseEntity<ChatResponse> create(@Valid @RequestBody ChatCreateRequest request) {
        log.info("Called create with BODY = {}", request);
        return ResponseEntity.ok(chatService.create(request));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ChatResponse> update(@PathVariable("id") Long id, @NotBlank String name) {
        log.info("Called update with id = {}, name = {}", id, name);
        return ResponseEntity.ok(chatService.update(id, name));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ChatDeleteResponse> delete(@PathVariable("id") Long id) {
        log.info("Called delete with id = {}", id);
        return ResponseEntity.ok(chatService.delete(id));
    }

}
