package fezlr.fluffy.feed.service;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.feed.dto.response.FeedDeleteResponse;
import fezlr.fluffy.feed.dto.response.FeedResponse;
import fezlr.fluffy.feed.entity.FeedEntity;
import fezlr.fluffy.feed.mapper.FeedMapper;
import fezlr.fluffy.feed.repository.FeedRepository;
import fezlr.fluffy.photo.service.PhotoStorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
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
        } else if (text.isEmpty()) {
            throw new IllegalStateException("One of fields should be filled");
        }

        FeedEntity entity = FeedEntity.builder()
                .user(customAuthService.getCurrentUser())
                .photoUrl(photoUrl)
                .text(text)
                .build();

        return feedMapper.toResponse(feedRepository.save(entity));
    }

    @Transactional
    public FeedResponse update(Long id, String text, MultipartFile photo, boolean removePhoto) {
        log.info("Called update with ID = {}, TEXT = {}, PHOTO = {}, REMOVEPHOTO = {}", id, text, photo, removePhoto);
        boolean isChanged = false;
        var feed = feedRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post is not found"));
        var currentUser = customAuthService.getCurrentUser();

        //check if it's not your post
        if (!currentUser.getId().equals(feed.getUser().getId())) {
            throw new IllegalStateException("Different account");
        }

        //if text changed - set new
        if ((!text.isBlank() || !feed.getPhotoUrl().isBlank()) && !Objects.equals(feed.getText(), text)) {
            isChanged = true;
            feed.setText(text);
        }

        //if photoUrl changed - set new
        if (photo != null && !removePhoto && !Objects.equals(feed.getPhotoUrl(), photo.toString())) {
            isChanged = true;
            feed.setPhotoUrl(photoStorageService.uploadPhoto(photo));
        }

        if (removePhoto && feed.getPhotoUrl() != null) {
//            photoStorageService.deletePhoto(photo);
            feed.setPhotoUrl(null);
        }

        //if changed - save
        if(isChanged) {
            return feedMapper.toResponse(feedRepository.save(feed));
        }

        return feedMapper.toResponse(feed);
    }

    @Transactional
    public FeedDeleteResponse delete(Long id) {
        var feed = feedRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post is not found"));
        var currentUser = customAuthService.getCurrentUser();

        //check if it's not your post
        if (!currentUser.getId().equals(feed.getUser().getId())) {
            throw new IllegalStateException("Different account");
        }

        feedRepository.deleteById(id);
        return new FeedDeleteResponse(id);
    }
}