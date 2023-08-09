package ca.sxxxi.twitter_clone_backend.service;
import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.entity.UserFollowEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import ca.sxxxi.twitter_clone_backend.repository.UserFollowsRepository;
import ca.sxxxi.twitter_clone_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileService {
    private JwtService jwtService;
    private UserRepository userRepo;
    private UserFollowsRepository userFollowRepo;
//    private PostRepository postRepo;

    public Optional<User> getUserById(String id) {
        return userRepo.getUserById(id).map(UserEntity::toModel);
    }

    public List<User> getUsersFollowedByUser(String followerId) {
        Optional<UserEntity> follower = userRepo.getUserById(followerId);
        return follower
                .map(user -> user
                        .getFollowing()
                        .stream()
                        .map(UserFollowEntity::getFollowee)
                        .map(UserEntity::toModel)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }

    public List<User> getFollowersOfUser(String userId) {
        Optional<UserEntity> follower = userRepo.getUserById(userId);
        return follower
                .map(user -> user
                        .getFollowers()
                        .stream()
                        .map(UserFollowEntity::getFollower)
                        .map(UserEntity::toModel)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }

    public void followUser(String followerId, String followedId) throws IllegalArgumentException {
        toggleFollowUser(followerId, followedId, uf -> { userFollowRepo.save(uf); });
    }

    public void unfollowUser(String followerId, String followedId) throws IllegalArgumentException {
        toggleFollowUser(followerId, followedId, uf -> { userFollowRepo.delete(uf); });
    }

    private void toggleFollowUser(String followerId, String followedId, Consumer<UserFollowEntity> consumer) throws IllegalArgumentException {
        Optional<UserEntity> oFollowed = userRepo.getUserById(followedId);
        Optional<UserEntity> oFollower = userRepo.getUserById(followerId);
        if (oFollowed.isPresent() && oFollower.isPresent()) {
            UserEntity follower = oFollower.get();
            UserEntity followed = oFollowed.get();
            UserFollowEntity uf = new UserFollowEntity(follower, followed);
            consumer.accept(uf);
        } else {
            throw new IllegalArgumentException("Not all parties exist");
        }
    }
}
