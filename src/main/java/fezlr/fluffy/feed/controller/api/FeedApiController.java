package fezlr.fluffy.feed.controller.api;

import fezlr.fluffy.feed.constant.FeedConstants;
import fezlr.fluffy.feed.dto.request.FeedRequest;
import fezlr.fluffy.feed.dto.response.FeedDeleteResponse;
import fezlr.fluffy.feed.dto.response.FeedResponse;
import fezlr.fluffy.feed.service.FeedService;
import fezlr.fluffy.photo.service.PhotoStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feed")
public class FeedApiController {
    private final FeedService feedService;
    private final PhotoStorageService photoStorageService;

    @GetMapping
    public ResponseEntity<List<FeedResponse>> findFeed(@RequestParam double seed, @RequestParam(defaultValue = FeedConstants.FIRST_PAGE_STRING) int page, @RequestParam(defaultValue = FeedConstants.SIZE_STRING) int size) {
        return ResponseEntity.ok(feedService.findRandom(seed, page, size));
    }

    //api logic for creating feed posts
    @PostMapping("/create")
    public ResponseEntity<FeedResponse> createFeed(@RequestParam(required = false) String text, @RequestParam(required = false) MultipartFile photo) {
        log.info("Called createFeed with PHOTO = {}, TEXT = {}", photo, text);
        return ResponseEntity.ok(feedService.create(text, photo));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<FeedResponse> updateFeed(@PathVariable("id") String id, @RequestParam(required = false) String text, @RequestParam(required = false) MultipartFile photo, @RequestParam(defaultValue = "false") boolean removePhoto) {
        return ResponseEntity.ok(feedService.update(Long.valueOf(id), text, photo, removePhoto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FeedDeleteResponse> deleteFeed(@PathVariable("id") String id) {
        log.info("Called deleteFeed with ID = {}", id);
        return ResponseEntity.ok(feedService.delete(Long.valueOf(id)));
    }

}
