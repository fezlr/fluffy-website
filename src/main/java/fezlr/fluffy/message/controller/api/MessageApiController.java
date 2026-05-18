package fezlr.fluffy.message.controller.api;

import fezlr.fluffy.message.dto.request.MessageRequest;
import fezlr.fluffy.message.dto.response.MessageResponse;
import fezlr.fluffy.message.dto.response.MessageDeleteResponse;
import fezlr.fluffy.message.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/messages")
public class MessageApiController {
    private final MessageService messageService;

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponse>> getByChatId(@PathVariable Long chatId) {
        return ResponseEntity.ok(messageService.getByChatId(chatId));
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.create(request));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable("id") Long id, @NotBlank String text) {
        return ResponseEntity.ok(messageService.update(id, text));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageDeleteResponse> delete(@PathVariable("id") Long id) {
       return ResponseEntity.ok(messageService.delete(id));
    }
}
