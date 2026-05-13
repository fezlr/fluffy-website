package fezlr.fluffy.profile.repository;

import fezlr.fluffy.profile.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    @Query("""
    SELECT p FROM ProfileEntity p
    WHERE p.id <> :id
    """)
    Page<ProfileEntity> findPotentialFriends(@Param("id") Long id, Pageable pageable);
}
