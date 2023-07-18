package ca.sxxxi.twitter_clone_backend.model.entity_models;

import ca.sxxxi.twitter_clone_backend.entity.CommentEntity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    private UUID id;
    private User author;
    private String content;
    private LocalDateTime dateCreated;
}
