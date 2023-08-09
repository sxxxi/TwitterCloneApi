package ca.sxxxi.twitter_clone_backend.repository.comments;

import ca.sxxxi.twitter_clone_backend.entity.comments.PostReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostReplyRepository extends JpaRepository<PostReplyEntity, UUID> {
    Optional<PostReplyEntity> getPostReplyEntityById(UUID id);
    Page<PostReplyEntity> getPostReplyEntitiesByPostOwner_Id(UUID postId, Pageable pageable);
}
