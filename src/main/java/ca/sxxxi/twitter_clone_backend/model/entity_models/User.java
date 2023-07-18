package ca.sxxxi.twitter_clone_backend.model.entity_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String id;
    private String pfp;
    private String firstName;
    private String lastName;
}
