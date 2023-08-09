package ca.sxxxi.twitter_clone_backend.entity.comments;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyEntity extends CommentEntity {
    @ManyToOne
    @JoinColumn(name = "comment_owner_id", referencedColumnName = "id")
    private CommentEntity commentOwner;

    public CommentReplyEntity(CommentEntity commentOwner, UserEntity author, String comment) {
        super(author, comment);
        this.commentOwner = commentOwner;
    }
}
