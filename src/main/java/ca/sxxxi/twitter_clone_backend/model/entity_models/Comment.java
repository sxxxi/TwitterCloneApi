package ca.sxxxi.twitter_clone_backend.model.entity_models;

import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    @NonNull
    private UUID id;
    @NonNull
    private User author;
    @NonNull
    private String content;
    @NonNull
    private Long dateCreated;
    private List<Comment> replies = null;
}
