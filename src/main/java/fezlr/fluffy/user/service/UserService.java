package fezlr.fluffy.user.service;

import fezlr.fluffy.auth.dto.request.RegisterRequest;
import fezlr.fluffy.auth.enums.Provider;
import fezlr.fluffy.common.service.CustomAuthService;
import fezlr.fluffy.token.entity.TokenEntity;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.dto.request.UserUpdateRequest;
import fezlr.fluffy.user.dto.response.UserUpdateResponse;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.enums.Role;
import fezlr.fluffy.user.mapper.UserUpdateMapper;
import fezlr.fluffy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final CustomAuthService customAuthService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserUpdateMapper userUpdateMapper;

    @Transactional
    public UserEntity create(UserRequest userRequest) {
        var userEntity = UserEntity
                .builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .password(passwordEncoder.encode(userRequest.password()))
                .provider(Provider.LOCAL)
                .role(Role.USER)
                .build();
        return userEntity;
    }

    @Transactional
    public UserEntity getByToken(TokenEntity tokenEntity) {
        return tokenEntity.getUser();
    }

    @Transactional
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Transactional
    public void changePassword(UserEntity userEntity, String password) {
        userEntity.setPassword(passwordEncoder.encode(password));
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User is not found"));
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public UserRequest createRequest(RegisterRequest request) {
        return new UserRequest(
                request.email(),
                request.username(),
                request.password()
        );
    }

    public UserUpdateResponse update(UserUpdateRequest request) {
        boolean isChanged = false;
        UserEntity entity = customAuthService.getCurrentUser();

        if (request.username() != null &&
                !request.username().isBlank() &&
                    !Objects.equals(request.username(), entity.getUsername()) &&
                        !userRepository.existsByUsername(request.username())) {
            isChanged = true;
            entity.setUsername(request.username());
        }

        if (isChanged) {
            return userUpdateMapper.toResponse(userRepository.save(entity));
        }

        return userUpdateMapper.toResponse(entity);
    }
}