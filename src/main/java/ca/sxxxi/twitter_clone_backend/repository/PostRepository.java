package ca.sxxxi.twitter_clone_backend.repository;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {
//public interface PostRepository extends PagingAndSortingRepository<PostEntity, UUID> {
    List<PostEntity> getPostsByAuthorId(String authorId);
    Optional<PostEntity> getPostById(UUID id);
}
