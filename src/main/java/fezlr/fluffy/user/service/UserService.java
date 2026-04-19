package fezlr.fluffy.user.service;

import fezlr.fluffy.auth.dto.request.RegisterRequest;
import fezlr.fluffy.auth.enums.Provider;
import fezlr.fluffy.token.entity.TokenEntity;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.enums.Role;
import fezlr.fluffy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public UserEntity create(UserRequest userRequest) {
        log.info("Called create with BODY = {}", userRequest);
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

    public UserRequest createRequest(RegisterRequest request) {
        return new UserRequest(
                request.email(),
                request.username(),
                request.password()
        );
    }
}