package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.service.JwtService;
import ca.sxxxi.twitter_clone_backend.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class FollowController {
    private JwtService jwtService;
    private ProfileService profileService;

    @PostMapping("/follow/{followeeId}")
    public ResponseEntity<String> followUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken, @PathVariable String followeeId) {
        try {
            String followerId = jwtService.getUsername(authToken);
            profileService.followUser(followerId, followeeId);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/unfollow/{followeeId}")
    public ResponseEntity<String> unfollowUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken, @PathVariable String followeeId) {
        try {
            String followerId = jwtService.getUsername(authToken);
            profileService.unfollowUser(followerId, followeeId);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
