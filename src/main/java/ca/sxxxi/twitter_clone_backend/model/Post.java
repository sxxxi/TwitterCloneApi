package ca.sxxxi.twitter_clone_backend.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Post implements Serializable {
    @NonNull
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String content;
    @NonNull
    private LocalDateTime dateCreated;
    private User author;
}
