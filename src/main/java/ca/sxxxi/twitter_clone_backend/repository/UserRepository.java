package ca.sxxxi.twitter_clone_backend.repository;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> getUserById(String id);
    Page<UserEntity> findAllByIdLikeIgnoreCase(String userNameLike, Pageable pageable);
    Page<UserEntity> findAllByFullNameContainingIgnoreCase(String fullNameContaining, Pageable pageable);
    Page<UserEntity> findAllByIdContainingIgnoreCaseOrFullNameContainingIgnoreCase(String userNameLike, String fullNameContaining, Pageable pageable);
}
