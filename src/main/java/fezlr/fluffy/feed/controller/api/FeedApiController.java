package fezlr.fluffy.feed.controller.api;

import fezlr.fluffy.feed.constant.FeedConstants;
import fezlr.fluffy.feed.dto.request.FeedRequest;
import fezlr.fluffy.feed.dto.response.FeedResponse;
import fezlr.fluffy.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feed")
public class FeedApiController {
    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<List<FeedResponse>> findFeed(@RequestParam double seed, @RequestParam(defaultValue = FeedConstants.FIRST_PAGE_STRING) int page, @RequestParam(defaultValue = FeedConstants.SIZE_STRING) int size) {
        return ResponseEntity.ok(feedService.findRandom(seed, page, size));
    }

    //api logic for creating feed posts
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FeedResponse> createFeed(@RequestParam(required = false) String text, @RequestParam(required = false) MultipartFile file) {
        return ResponseEntity.ok(feedService.create(text, file));
    }

}
