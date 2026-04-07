package fezlr.fluffy.token.repository;

import fezlr.fluffy.token.entity.TokenEntity;
import fezlr.fluffy.token.enums.TokenType;
import fezlr.fluffy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);

    Optional<TokenEntity> findByTokenAndUserEmail(String token, String email);

    @Query("SELECT t FROM TokenEntity t JOIN FETCH t.user WHERE t.token = :token")
    Optional<TokenEntity> findByTokenWithUser(String token);

    @Modifying
    @Transactional
    @Query("UPDATE TokenEntity t SET t.isActive = false WHERE t.user = :user AND t.tokenType = :tokenType")
    void deactivateAllByUserAndType(UserEntity user, TokenType tokenType);
}
