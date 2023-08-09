package ca.sxxxi.twitter_clone_backend.model.form_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest implements Serializable {
//    private String posterId;
    private String title;
    private String content;
}
