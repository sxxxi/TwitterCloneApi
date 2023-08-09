package ca.sxxxi.twitter_clone_backend.service;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.Post;
import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import ca.sxxxi.twitter_clone_backend.repository.UserFeedRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserFeedService {
    private ProfileService profileService;
    private UserFeedRepository userFeedRepository;
    public Page<Post> getFollowedUserPostsBeforeDate(String followerId, Long epochSeconds, PageRequest request) {
        // Get users followed by user
        List<String> followed = profileService
                .getUsersFollowedByUser(followerId)
                .stream().map(User::getId)
                .collect(Collectors.toList());
        followed.add(followerId);
        Date dateTime = Date.from(Instant.ofEpochSecond(epochSeconds));
        System.out.println(request.toString());
        return userFeedRepository
                .getPostEntitiesByAuthorIdInAndDateCreatedBefore(followed, dateTime, request)
                .map(PostEntity::toModel);
    }
}
