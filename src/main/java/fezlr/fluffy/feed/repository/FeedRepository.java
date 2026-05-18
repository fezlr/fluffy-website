package fezlr.fluffy.feed.repository;

import fezlr.fluffy.feed.entity.FeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
    @Query(value = " SELECT * FROM feeds ORDER BY RANDOM() * :seed LIMIT :size OFFSET :offset", nativeQuery = true)
    List<FeedEntity> findRandom(@Param("seed") double seed, @Param("size") int size, @Param("offset") int offset);
}
