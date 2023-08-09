package ca.sxxxi.twitter_clone_backend.model.form_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse implements Serializable {
    private String token;
}
