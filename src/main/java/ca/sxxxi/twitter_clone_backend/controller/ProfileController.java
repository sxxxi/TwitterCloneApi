package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import ca.sxxxi.twitter_clone_backend.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private ProfileService profileService;

    @GetMapping("/{uid}")
    public ResponseEntity<User> getProfileByUserId(@PathVariable String uid) {
        return profileService.getUserById(uid).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/following/{followerId}")
    public ResponseEntity<List<User>> getUserFollowed(@PathVariable String followerId) {
        return ResponseEntity.ok(profileService.getUsersFollowedByUser(followerId));
    }

    @GetMapping("/followers/{uid}")
    public ResponseEntity<?> getUserFollowers(@PathVariable String uid) {
        return ResponseEntity.ok(profileService.getFollowersOfUser(uid));
    }

//    @GetMapping("/following/{uid}")
//    public ResponseEntity<?> getUserFollowing(@PathVariable String uid) {
//        return ResponseEntity.ok().build();
//    }
}
