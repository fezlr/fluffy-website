package fezlr.fluffy.message.repository;

import fezlr.fluffy.message.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m WHERE m.chat.id = :chatId ORDER BY m.createdAt ASC")
    List<MessageEntity> findByChatId(@Param("chatId") Long chatId);
}
