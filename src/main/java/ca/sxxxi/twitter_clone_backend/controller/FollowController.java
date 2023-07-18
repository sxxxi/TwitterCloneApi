package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class FollowController {
    private ProfileService profileService;

    @GetMapping("/followed/{followerId}")
    public ResponseEntity<List<UserEntity>> getUserFollowed(@PathVariable String followerId) {
        return ResponseEntity.ok(profileService.getUsersFollowedByUser(followerId));
    }


    @PostMapping("/follow/{followerId}/{followeeId}")
    public ResponseEntity<String> followUser(@PathVariable String followerId, @PathVariable String followeeId) {
        try {
            profileService.followUser(followerId, followeeId);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/unfollow/{followerId}/{followeeId}")
    public ResponseEntity<String> unfollowUser(@PathVariable String followerId, @PathVariable String followeeId) {
        try {
            profileService.unfollowUser(followerId, followeeId);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
