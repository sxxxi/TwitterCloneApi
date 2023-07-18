package ca.sxxxi.twitter_clone_backend.model.entity_models;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Post implements Serializable {
    @NonNull
    private UUID id;
    @NonNull
    private String title;
    @NonNull
    private String content;
    @NonNull
    private LocalDateTime dateCreated;

    @NonNull
    private User author;
    private List<Comment> comments;
}
