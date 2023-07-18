package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.model.Post;
import ca.sxxxi.twitter_clone_backend.model.PostCreate;
import ca.sxxxi.twitter_clone_backend.model.PostUpdate;
import ca.sxxxi.twitter_clone_backend.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostsController {
    private PostService postService;

    @GetMapping("/user/{uid}")
    public ResponseEntity<List<Post>> getPostsByAuthorId(@PathVariable String uid) {
        return ResponseEntity.ok(postService.getPostByAuthorId(uid));
    }

    @GetMapping("/pid/{pid}")
    public ResponseEntity<Optional<Post>> getPostById(@PathVariable Long pid) {
        return ResponseEntity.ok(postService.getPostByPostId(pid));
    }

    @DeleteMapping("/delete/{pid}")
    public ResponseEntity<?> deletePostById(@PathVariable Long pid) {
        try {
            postService.deletePostById(pid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody PostCreate postForm) {
        Optional<PostEntity> oPost = postService.createPost(postForm);
        if (oPost.isPresent()) {
            PostEntity post = oPost.get();
            return ResponseEntity.ok(post.toModel());
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/update/{pid}")
    public ResponseEntity<PostUpdate> startPostUpdate(@PathVariable Long pid) {
        Optional<PostUpdate> oPostUpdate = postService.startPostUpdate(pid);
        return oPostUpdate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/update")
    public ResponseEntity<Post> updatePost(@RequestBody PostUpdate postUpdate) {
        Optional<Post> oPostUpdate = postService.commitPostUpdate(postUpdate);
        return oPostUpdate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
