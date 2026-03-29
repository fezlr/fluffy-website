package fezlr.fluffy.token.service;

import fezlr.fluffy.token.dto.response.TokenResponse;
import fezlr.fluffy.token.entity.TokenEntity;
import fezlr.fluffy.token.enums.TokenType;
import fezlr.fluffy.token.mapper.TokenMapper;
import fezlr.fluffy.token.repository.TokenRepository;
import fezlr.fluffy.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {
    @Value("${spring.token.expired-time-minutes}")
    private Long expiredPlusMinutes;
    private final TokenMapper tokenMapper;
    private final TokenRepository tokenRepository;

    @Transactional
    public TokenResponse saveCode(UserEntity userEntity, TokenType tokenType) {
        log.info("Called saveCode with BODY = {}", userEntity);
        String tokenCode = String.format("%06d", new SecureRandom().nextInt(999999));
        var tokenEntity = TokenEntity
                .builder()
                .token(tokenCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expiredPlusMinutes))
                .user(userEntity)
                .tokenType(TokenType.CREATE_USER)
                .build();

        tokenRepository.save(tokenEntity);
        return tokenMapper.toResponse(tokenEntity);
    }

    @Transactional
    public TokenResponse saveLink(UserEntity userEntity, TokenType tokenType) {
        log.info("Called saveLink with BODY = {}", userEntity);
        var tokenEntity = TokenEntity
                .builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expiredPlusMinutes))
                .user(userEntity)
                .tokenType(tokenType)
                .build();

        tokenRepository.save(tokenEntity);
        return tokenMapper.toResponse(tokenEntity);
    }

    @Transactional
    public TokenResponse confirmWithEntity(TokenEntity entity, String token) {
        entity.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(entity);
        return tokenMapper.toResponse(entity);
    }

    @Transactional
    public TokenEntity validate(String token) {
        TokenEntity entity = findByToken(token);

        if(entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token is expired");
        }

        if(entity.getConfirmedAt() != null) {
            throw new IllegalArgumentException("Token is already used");
        }

        return entity;
    }

    @Transactional
    public TokenEntity validate(String token, String email) {
        TokenEntity entity = findByTokenAndUserEmail(token, email);

        if(entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token is expired");
        }

        if(entity.getConfirmedAt() != null) {
            throw new IllegalArgumentException("Token is already used");
        }

        return entity;
    }

    @Transactional
    public TokenEntity findByToken(String token) {
        return tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    @Transactional
    public TokenEntity findByTokenAndUserEmail(String token, String email) {
        return tokenRepository
                .findByTokenAndUserEmail(token, email)
                .orElseThrow(() -> new EntityNotFoundException("Entity or email not found"));
    }
}