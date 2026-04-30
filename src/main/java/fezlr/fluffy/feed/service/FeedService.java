package fezlr.fluffy.feed.service;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.feed.dto.response.FeedResponse;
import fezlr.fluffy.feed.entity.FeedEntity;
import fezlr.fluffy.feed.mapper.FeedMapper;
import fezlr.fluffy.feed.repository.FeedRepository;
import fezlr.fluffy.photo.service.PhotoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedService {
    private final CustomAuthService customAuthService;
    private final PhotoStorageService photoStorageService;
    private final FeedRepository feedRepository;
    private final FeedMapper feedMapper;

    public List<FeedResponse> findRandom(double seed, int page, int size) {
        int offset = page * size;
        return feedRepository.findRandom(seed, size, offset)
                .stream()
                .map(feedMapper::toResponse)
                .toList();
    }

    @Transactional
    public FeedResponse create(String text, MultipartFile file) {
        String photoUrl = null;
        if (file != null && !file.isEmpty()) {
            photoUrl = photoStorageService.uploadPhoto(file);
        }

        FeedEntity entity = FeedEntity.builder()
                .user(customAuthService.getCurrentUser())
                .photoUrl(photoUrl)
                .text(text)
                .build();

        return feedMapper.toResponse(feedRepository.save(entity));
    }
}