package ca.sxxxi.twitter_clone_backend.entity;

import ca.sxxxi.twitter_clone_backend.model.User;
import ca.sxxxi.twitter_clone_backend.utils.Mappable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Cacheable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Mappable<User> {
    @Id
    private String id;
    private String pfp;
    private String firstName;
    private String lastName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "follower")
    private List<UserFollowEntity> following;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "followee")
    private List<UserFollowEntity> followers;

    @Override
    public User toModel() {
        return new User(id, pfp, firstName, lastName);
    }
}
