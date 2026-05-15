package fezlr.fluffy.profile.service;

import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.feed.dto.response.FeedResponse;
import fezlr.fluffy.profile.dto.request.ProfileRequest;
import fezlr.fluffy.profile.dto.response.ProfilePotentialFriendsResponse;
import fezlr.fluffy.profile.dto.response.ProfileResponse;
import fezlr.fluffy.profile.entity.ProfileEntity;
import fezlr.fluffy.profile.mapper.ProfileMapper;
import fezlr.fluffy.profile.mapper.ProfilePotentialFriendsMapper;
import fezlr.fluffy.profile.repository.ProfileRepository;
import fezlr.fluffy.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {
    private final CustomAuthService customAuthService;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ProfilePotentialFriendsMapper profilePotentialFriendsMapper;

    public ProfileEntity create(UserEntity userEntity) {
        return ProfileEntity.builder()
                .user(userEntity)
                .build();
    }

    public ProfileResponse findProfile(Long id) {
        return profileMapper.toResponse(profileRepository
                                .findById(id)
                                .orElseThrow(() ->
                                        new EntityNotFoundException("Profile is not found")));
    }

    @Transactional
    public ProfileResponse edit(ProfileRequest request) {
        log.info("Called edit with BODY = {}", request);

        boolean changed = false;
        ProfileEntity profileEntity = customAuthService.getCurrentUser().getProfile();

        if (!Objects.equals(request.firstName(), profileEntity.getFirstName())) {
            profileEntity.setFirstName(request.firstName());
            changed = true;
        }

        if (!Objects.equals(request.lastName(), profileEntity.getLastName())) {
            profileEntity.setLastName(request.lastName());
            changed = true;
        }

        if (!Objects.equals(request.birthDate(), profileEntity.getBirthDate())) {
            profileEntity.setBirthDate(request.birthDate());
            changed = true;
        }

        if (!Objects.equals(request.gender(), profileEntity.getGender())) {
            profileEntity.setGender(request.gender());
            changed = true;
        }

        if (!Objects.equals(request.city(), profileEntity.getCity())) {
            profileEntity.setCity(request.city());
            changed = true;
        }

        if (!Objects.equals(request.aboutMe(), profileEntity.getAboutMe())) {
            profileEntity.setAboutMe(request.aboutMe());
            changed = true;
        }

        //check whether is complete or not
        if (isFirstNameAndLastNameComplete(profileEntity)) {
            profileEntity.setComplete(true);
        } else {
            profileEntity.setComplete(false);
        }

        //save & return
        if (changed) {
            return profileMapper.toResponse(profileRepository.save(profileEntity));
        }

        //return
        return profileMapper.toResponse(profileEntity);
    }

    public boolean isFirstNameAndLastNameComplete(ProfileEntity profile) {
        return profile.getFirstName() != null && !profile.getFirstName().isBlank() &&
                profile.getLastName() != null && !profile.getLastName().isBlank();
    }

    @Transactional
    public void uploadAndSaveProfilePhoto(String url) {
        ProfileEntity profile = customAuthService.getCurrentUser().getProfile();
        profile.setMainPhotoUrl(url);
        save(profile);
    }

    @Transactional
    public void save(ProfileEntity profile) {
        profileRepository.save(profile);
    }

    public Page<ProfilePotentialFriendsResponse> findPotentialFriends(Long id, Pageable pageable) {
        log.info("Called findPotentialFriends with ID = {}, PAGEABLE = {}", id, pageable);
        return profileRepository.findPotentialFriends(id, pageable)
                .map(profilePotentialFriendsMapper::toResponse);
    }
}