package fezlr.fluffy.profile.controller.api;

import fezlr.fluffy.profile.dto.request.ProfileRequest;
import fezlr.fluffy.profile.dto.response.ProfileResponse;
import fezlr.fluffy.profile.service.ProfileService;
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
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/setup")
    public ResponseEntity<ProfileResponse> setup(@Valid @RequestBody ProfileRequest request) {
        log.info("Called setup()");
        return ResponseEntity.ok(profileService.setup(request));
    }
}