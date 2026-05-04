package fezlr.fluffy.profile.controller.api;

import fezlr.fluffy.photo.property.CloudinaryProperties;
import fezlr.fluffy.photo.service.PhotoStorageService;
import fezlr.fluffy.profile.dto.request.ProfileRequest;
import fezlr.fluffy.profile.dto.response.ProfileResponse;
import fezlr.fluffy.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final PhotoStorageService photoStorageService;

    @PostMapping("/edit")
    public ResponseEntity<ProfileResponse> edit(@Valid @RequestBody ProfileRequest request) {
        log.info("Called setup()");
        return ResponseEntity.ok(profileService.edit(request));
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<Map<String, String>> uploadPhoto(@Valid @RequestParam("file") MultipartFile file) {
        log.info("Called uploadPhoto() with BODY = {}", file);
        String url = photoStorageService.uploadPhoto(file);
        profileService.uploadAndSaveProfilePhoto(url);
        return ResponseEntity.ok(Map.of("url", url));
    }
}