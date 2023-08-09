package ca.sxxxi.twitter_clone_backend.entity.comments;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.Comment;
import ca.sxxxi.twitter_clone_backend.utils.Mappable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    @Override
    public Comment toModel() {
        return new Comment(id, author.toModel(), content, dateCreated.toEpochSecond(ZoneOffset.UTC));
    }
}
