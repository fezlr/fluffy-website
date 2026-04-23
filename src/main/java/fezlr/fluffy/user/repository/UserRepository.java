package fezlr.fluffy.user.repository;

import fezlr.fluffy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :value OR u.email = :value")
    Optional<UserEntity> findByUsernameOrEmail(@Param("value") String value);

    @Query("SELECT u.profile FROM UserEntity u WHERE u.id = :userId")
    Optional<Long> findProfileByUserId(@Param("userId") Long userId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
