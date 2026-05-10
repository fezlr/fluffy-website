package fezlr.fluffy.friend_request.repository;

import fezlr.fluffy.friend_request.entity.FriendRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {

    boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId);

    Optional<FriendRequestEntity> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    @Query("""
        SELECT f
        FROM FriendEntity f
        WHERE f.user.id <> :myId
        AND EXISTS (
            SELECT 1
            FROM FriendRequestEntity v1
            WHERE v1.senderId = :myId
            AND v1.receiverId = f.user.id
        )
        AND EXISTS (
            SELECT 1
            FROM FriendRequestEntity v2
            WHERE v2.senderId = f.user.id
            AND v2.receiverId = :myId
        )
        """)
    Page<FriendRequestEntity> findMutualFriendRequests(@Param("myId") Long id, Pageable pageable);

    Page<FriendRequestEntity> findAllBySenderId(Long senderId, Pageable pageable);

    Page<FriendRequestEntity> findAllByReceiverId(Long receiverId, Pageable pageable);

    @Modifying
    void deleteBySenderIdAndReceiverId(Long senderId, Long receiverId);
}