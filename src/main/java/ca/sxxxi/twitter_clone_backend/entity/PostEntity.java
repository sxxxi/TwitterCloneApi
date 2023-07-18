package ca.sxxxi.twitter_clone_backend.entity;

import ca.sxxxi.twitter_clone_backend.model.entity_models.Post;
import ca.sxxxi.twitter_clone_backend.utils.Mappable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class PostEntity implements Mappable<Post>, Comparable<PostEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NonNull
    private String title;
    @NonNull
    private String content;
    @NonNull
    private LocalDateTime dateCreated;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private UserEntity author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postOwner")
    private List<CommentEntity> comments;

    @Override
    public int compareTo(PostEntity other) {
        if (dateCreated.isAfter(other.dateCreated)) {
            return -1;
        } else if (dateCreated.isBefore(other.dateCreated)) {
            return 1;
        } else return 0;
    }

    @Override
    public Post toModel() {
        return new Post(id, title, content, dateCreated, author.toModel());
    }
}
