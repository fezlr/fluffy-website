package fezlr.fluffy.token.service;

import fezlr.fluffy.token.dto.response.TokenResponse;
import fezlr.fluffy.token.entity.TokenEntity;
import fezlr.fluffy.token.mapper.TokenMapper;
import fezlr.fluffy.token.repository.TokenRepository;
import fezlr.fluffy.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {
    @Value("${spring.token.expired-time-minutes}")
    private Long expiredPlusMinutes;
    private final TokenMapper tokenMapper;
    private final TokenRepository tokenRepository;

    @Transactional
    public TokenResponse save(UserEntity userEntity) {
        log.info("Called save with BODY = {}", userEntity);
        String tokenCode = String.format("%06d", new SecureRandom().nextInt(999999));
        var tokenEntity = TokenEntity
                .builder()
                .token(tokenCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expiredPlusMinutes))
                .user(userEntity)
                .build();

        tokenRepository.save(tokenEntity);
        return tokenMapper.toResponse(tokenEntity);
    }
}