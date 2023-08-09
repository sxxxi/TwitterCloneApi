package ca.sxxxi.twitter_clone_backend.repository.comments;

import ca.sxxxi.twitter_clone_backend.entity.comments.CommentEntity;
import ca.sxxxi.twitter_clone_backend.entity.comments.PostReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentsRepository extends PagingAndSortingRepository<CommentEntity, UUID> {
    Optional<CommentEntity> getCommentEntityById(UUID id);
}
