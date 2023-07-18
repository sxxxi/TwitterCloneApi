package ca.sxxxi.twitter_clone_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Cacheable
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@IdClass(UserFollowEntity.UserFollowId.class)
public class UserFollowEntity {
    @NonNull
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id")
    private UserEntity follower;

    @NonNull
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "followee_id")
    private UserEntity followee;

    public static class UserFollowId implements Serializable {
        private UserEntity follower;
        private UserEntity followee;
    }
}
