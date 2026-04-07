package fezlr.fluffy.token.mapper;

import fezlr.fluffy.token.dto.response.TokenResponse;
import fezlr.fluffy.token.entity.TokenEntity;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {
    //TODO: builder
    public TokenResponse toResponse(TokenEntity tokenEntity) {
        return new TokenResponse(
                tokenEntity.getId(),
                tokenEntity.getToken(),
                tokenEntity.getCode(),
                tokenEntity.getCreatedAt(),
                tokenEntity.getExpiresAt(),
                tokenEntity.getConfirmedAt(),
                tokenEntity.getUser().getId(),
                tokenEntity.getTokenType(),
                tokenEntity.isActive()
        );
    }
}