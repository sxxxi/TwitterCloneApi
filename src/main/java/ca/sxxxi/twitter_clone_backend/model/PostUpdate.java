package ca.sxxxi.twitter_clone_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdate implements Serializable {
    private Long id;
    private String title;
    private String content;
}
