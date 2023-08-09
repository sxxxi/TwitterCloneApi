package ca.sxxxi.twitter_clone_backend.service;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import ca.sxxxi.twitter_clone_backend.model.response_models.UserSearchResult;
import ca.sxxxi.twitter_clone_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchService {
    private ProfileService profileService;
    private UserRepository userRepo;

    public Page<UserSearchResult> findUserWithUsernameOrName(String followerId, String searchTerm, Pageable pageRequest) {
        // Prepare list of users followed
        List<User> followedUsers = profileService.getUsersFollowedByUser(followerId);

        return userRepo
                .findAllByIdContainingIgnoreCaseOrFullNameContainingIgnoreCase(searchTerm, searchTerm, pageRequest)
                .map(UserEntity::toModel)
                .map(u -> new UserSearchResult(u, followedUsers.contains(u)));
    }
}
