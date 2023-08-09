package ca.sxxxi.twitter_clone_backend.model.form_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequest implements Serializable {
    private UUID id;
    private String title;
    private String content;
}
