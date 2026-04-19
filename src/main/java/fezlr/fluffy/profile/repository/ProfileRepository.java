package fezlr.fluffy.profile.repository;

import fezlr.fluffy.profile.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
}
