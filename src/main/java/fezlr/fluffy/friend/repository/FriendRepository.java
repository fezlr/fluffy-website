package fezlr.fluffy.friend.repository;

import fezlr.fluffy.friend.entity.FriendEntity;
import fezlr.fluffy.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendEntity, Long> {

    @Modifying
    Optional<FriendEntity> deleteByUserId(Long userId);

    @Modifying
    Optional<FriendEntity> deleteByFriendId(Long friendId);

    @Query("""
        SELECT f
        FROM FriendEntity f
        WHERE f.user.id <> :myId
        """)
    Page<FriendEntity> findFriends(@Param("myId") Long id, Pageable pageable);

    boolean existsByUserAndFriend(UserEntity senderEntity, UserEntity receiverEntity);

    @Query("""
        SELECT COUNT(f) > 0
        FROM FriendEntity f
        WHERE f.user.id = :senderId AND f.friend.id = :receiverId
            """)
    boolean existsBySenderIdAndReceiverId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}