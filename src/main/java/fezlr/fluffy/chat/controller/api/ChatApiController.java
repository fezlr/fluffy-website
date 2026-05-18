package fezlr.fluffy.chat.controller.api;

import fezlr.fluffy.chat.dto.request.ChatCreateRequest;
import fezlr.fluffy.chat.dto.request.ChatDirectRequest;
import fezlr.fluffy.chat.dto.request.ChatUpdateRequest;
import fezlr.fluffy.chat.dto.response.ChatDeleteResponse;
import fezlr.fluffy.chat.dto.response.ChatResponse;
import fezlr.fluffy.chat.service.ChatService;
import fezlr.fluffy.photo.service.PhotoStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatApiController {
    private final PhotoStorageService photoStorageService;
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatResponse>> allChatsByCurrentUser() {
        return ResponseEntity.ok(chatService.findAllChatsByCurrentUser());
    }

    @PostMapping("/create")
    public ResponseEntity<ChatResponse> create(@Valid @RequestBody ChatCreateRequest request) {
        return ResponseEntity.ok(chatService.create(request));
    }

    @PostMapping("/direct")
    public ResponseEntity<ChatResponse> getOrCreateDirect(@Valid @RequestBody ChatDirectRequest request){
        return ResponseEntity.ok(chatService.getOrCreateDirectChat(request));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ChatResponse> update(@PathVariable("id") Long id, @RequestBody ChatUpdateRequest request) {
        return ResponseEntity.ok(chatService.update(id, request));
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<Map<String, String>> uploadPhoto(@RequestParam("file") MultipartFile file) {
        String url = photoStorageService.uploadPhoto(file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ChatDeleteResponse> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(chatService.delete(id));
    }
}
