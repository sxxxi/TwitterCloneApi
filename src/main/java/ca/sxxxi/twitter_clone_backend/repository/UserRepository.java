package ca.sxxxi.twitter_clone_backend.repository;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> getUserById(String id);
}
