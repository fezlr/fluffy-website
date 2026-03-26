package fezlr.fluffy.user.service;

import fezlr.fluffy.auth.exception.EmailAlreadyExistsException;
import fezlr.fluffy.auth.exception.UsernameAlreadyExistsException;
import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.dto.response.UserResponse;
import fezlr.fluffy.user.entity.UserEntity;
import fezlr.fluffy.user.mapper.UserMapper;
import fezlr.fluffy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse save(UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.email())) {
            throw new EmailAlreadyExistsException("Email is already taken");
        }

        if(userRepository.existsByUsername(userRequest.username())) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }

        var entity = UserEntity.builder()
                .id(userRequest.id())
                .username(userRequest.username())
                .email(userRequest.email())
                .password(userRequest.password())
                .role(userRequest.role()).build();

        userRepository.save(entity);
        return userMapper.toResponse(entity);
    }
}
