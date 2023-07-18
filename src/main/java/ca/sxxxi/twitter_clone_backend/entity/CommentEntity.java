package ca.sxxxi.twitter_clone_backend.entity;

import ca.sxxxi.twitter_clone_backend.model.entity_models.Comment;
import ca.sxxxi.twitter_clone_backend.utils.Mappable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class CommentEntity implements Mappable<Comment> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity author;
    @NonNull
    private String content;
    private LocalDateTime dateCreated = LocalDateTime.now();

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_owner")
    private PostEntity postOwner;

    @Override
    public Comment toModel() {
        return new Comment(id, author.toModel(), content, dateCreated);
    }
}
