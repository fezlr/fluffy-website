package fezlr.fluffy.friend.repository;

import fezlr.fluffy.friend.entity.FriendEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<FriendEntity, Long> {


    @Query("""
        SELECT f
        FROM FriendEntity f
        WHERE f.user.id <> :myId
        """)
    Page<FriendEntity> findFriends(@Param("myId") Long id, Pageable pageable);
}