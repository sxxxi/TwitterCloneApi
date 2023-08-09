package ca.sxxxi.twitter_clone_backend.service;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.entity.comments.CommentEntity;
import ca.sxxxi.twitter_clone_backend.entity.comments.CommentReplyEntity;
import ca.sxxxi.twitter_clone_backend.entity.comments.PostReplyEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.Comment;
import ca.sxxxi.twitter_clone_backend.repository.PostRepository;
import ca.sxxxi.twitter_clone_backend.repository.comments.CommentReplyRepository;
import ca.sxxxi.twitter_clone_backend.repository.comments.CommentsRepository;
import ca.sxxxi.twitter_clone_backend.repository.comments.PostReplyRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentsService {
    private PostRepository postRepo;
    private PostReplyRepository postReplyRepo;
    private CommentReplyRepository commentReplyRepo;
    private CommentsRepository commentsRepository;

    public Comment getCommentById(UUID id) throws IllegalArgumentException {
        return commentsRepository.getCommentEntityById(id)
                .map(CommentEntity::toModel)
                .orElseThrow(() -> new IllegalArgumentException("Comment with provided ID not found"));
    }

    public Optional<Page<Comment>> getComments(UUID postId, Integer depth, PageRequest prPageRequest) {
        // Verify that post exists
        if (postId == null || !postRepo.existsById(postId)) {
            throw new IllegalArgumentException("Presented post ID must not be null and exist");
        }

        // Depth must not get larger than 8. (magic number lol)
        if (depth > 8) {
            throw new IllegalArgumentException("Depth must not get larger than 8. Call this function again when needed.");
        }

        // Get comment replies - initially send 3 comment replies per page
        PageRequest cmPageRequest = PageRequest.of(0, 3).withSort(Sort.Direction.DESC, "dateCreated");

        // Get comment replies for each postReplyEntity
        Page<Comment> topLevelDomainComments = postReplyRepo
                .getPostReplyEntitiesByPostOwner_Id(postId, prPageRequest)
                .map(CommentEntity::toModel);
        for (Comment comment : topLevelDomainComments) {
            comment.setReplies(getCommentReplies(comment, cmPageRequest, depth));
        }

        return Optional.of(topLevelDomainComments);
    }

    // TODO: Extract if block that handles the case where depth is zero
    public Page<Comment> getPagedCommentReplies(Comment comment, PageRequest pageRequest, Integer depth) {
        Page<Comment> commentReplies = commentReplyRepo
                .getCommentReplyEntitiesByCommentOwner_Id(comment.getId(), pageRequest)
                .map(CommentEntity::toModel);

        if (depth == 0) {
            // If still has replies, return null, else return empty list to indicate that it's the tail
            if (commentReplies.isEmpty()) {
                return commentReplies;
            } else {
                return null;
            }
        } else {
            commentReplies.forEach(c -> {
                c.setReplies(getCommentReplies(c, pageRequest, depth - 1));
            });

            return commentReplies;
        }
    }

    public List<Comment> getCommentReplies(Comment comment, PageRequest pageRequest, Integer depth) {
        List<Comment> commentReplies = commentReplyRepo
                .getCommentReplyEntitiesByCommentOwner_Id(comment.getId(), pageRequest)
                .map(CommentEntity::toModel).stream().toList();
        comment.setReplies(commentReplies);

        if (depth == 0) {
            // If still has replies, return null, else return empty list to indicate that it's the tail
            if (commentReplies.isEmpty()) {
                return commentReplies;
            } else {
                return null;
            }
        } else {
            commentReplies.forEach(c -> {
                c.setReplies(getCommentReplies(c, pageRequest, depth - 1));
            });

            return commentReplies;
        }

    }

    private UUID replyToComment(UUID recipient, UserEntity author, String comment) {
        CommentEntity recipientComment = postReplyRepo.getPostReplyEntityById(recipient).orElse(null);
        if (recipientComment == null) recipientComment = commentReplyRepo.getCommentReplyEntityById(recipient)
                        .orElseThrow(() -> new IllegalArgumentException("Comment deleted or does not exist."));

        return commentReplyRepo.save(new CommentReplyEntity(recipientComment, author, comment)).getId();
    }

    private UUID replyToPost(UUID postId, UserEntity author, String comment) {
        PostEntity recipientPost = postRepo.getPostById(postId).orElseThrow(() -> {
            String message = "Provided post ID is invalid.";
            return new IllegalArgumentException(message);
        });
        return postReplyRepo.save(new PostReplyEntity(recipientPost, author, comment)).getId();
    }

    public UUID postComment(@NonNull UUID postId, UUID recipient, @NonNull UserEntity author, @NonNull String rawComment) {
        // Don't accept comment when comment is empty
        var comment = rawComment.trim();

        if (comment.isEmpty()) {
            throw new IllegalArgumentException("Comment must not be empty.");
        }

        if (recipient == null) {        // Comment is top level
            return replyToPost(postId, author, comment);
        } else {                        // Comment is a reply to a comment
            return replyToComment(recipient, author, comment);
        }
    }
}
