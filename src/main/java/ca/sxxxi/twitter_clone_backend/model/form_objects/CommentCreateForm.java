package ca.sxxxi.twitter_clone_backend.model.form_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CommentCreateForm implements Serializable {
    private UUID postId;
    private UUID recipientId;
    private String authorId;
    private String content;
}
