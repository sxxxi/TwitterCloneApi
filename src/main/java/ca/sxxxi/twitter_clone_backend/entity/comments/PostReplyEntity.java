package ca.sxxxi.twitter_clone_backend.entity.comments;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReplyEntity extends CommentEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_owner")
    private PostEntity postOwner;

    public PostReplyEntity(PostEntity postEntity, UserEntity author, String content) {
        super(author, content);
        this.postOwner = postEntity;
    }
}
