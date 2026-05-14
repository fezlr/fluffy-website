package fezlr.fluffy.chat.repository;

import fezlr.fluffy.chat.dto.response.ChatResponse;
import fezlr.fluffy.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    @Query("""
    SELECT c FROM ChatEntity c
    WHERE c.userOne.id = :userId OR c.userTwo.id = :userId
    """)
    List<ChatEntity> findAllByUserId(@Param("userId") Long userId);
}
