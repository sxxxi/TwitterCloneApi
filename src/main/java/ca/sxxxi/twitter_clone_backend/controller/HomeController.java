package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feed")
@AllArgsConstructor
public class HomeController {
    private PostService postService;

    @GetMapping("/greet")
    public String greet() {
        return "Hello there";
    }

    @GetMapping({"/{userId}", "{userId}"})
    public ResponseEntity<List<PostEntity>> getFeed(@PathVariable String userId) {
        return ResponseEntity.ok(postService.getFeed(userId));
    }
}
