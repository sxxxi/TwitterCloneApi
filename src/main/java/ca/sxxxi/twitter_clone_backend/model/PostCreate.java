package ca.sxxxi.twitter_clone_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreate implements Serializable {
    private String posterId;
    private String title;
    private String content;
}
