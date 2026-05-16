package fezlr.fluffy.chat.repository;

import fezlr.fluffy.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    @Query("""
    SELECT c FROM ChatEntity c
    WHERE c.userOne.id = :userId OR c.userTwo.id = :userId
    """)
    List<ChatEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT COUNT(c)>0 FROM ChatEntity c
    WHERE
    (c.userOne.id = :userOneId and c.userTwo.id = :userTwoId)
    OR
    (c.userOne.id = :userTwoId and c.userTwo.id = :userOneId)
    """)
    boolean existsMutualByUserOneIdAndUserTwoId(Long userOneId, Long userTwoId);


    @Query("""
    SELECT c FROM ChatEntity c
    WHERE
    (c.userOne.id = :userOneId and c.userTwo.id = :userTwoId)
    OR
    (c.userOne.id = :userTwoId and c.userTwo.id = :userOneId)
    """)
    Optional<ChatEntity> findMutualByUserOneIdAndUserTwoId(Long userOneId, Long userTwoId);
}
