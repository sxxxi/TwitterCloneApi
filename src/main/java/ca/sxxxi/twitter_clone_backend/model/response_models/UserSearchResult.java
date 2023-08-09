package ca.sxxxi.twitter_clone_backend.model.response_models;

import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResult {
    private User userInfo;
    private Boolean followed;
}
