package ca.sxxxi.twitter_clone_backend.repository.comments;

import ca.sxxxi.twitter_clone_backend.entity.comments.CommentReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentReplyRepository extends JpaRepository<CommentReplyEntity, UUID> {
    Optional<CommentReplyEntity> getCommentReplyEntityById(UUID id);
    Page<CommentReplyEntity> getCommentReplyEntitiesByCommentOwner_Id(UUID commentId, Pageable pageable);
}
