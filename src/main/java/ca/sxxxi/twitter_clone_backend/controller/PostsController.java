package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.Comment;
import ca.sxxxi.twitter_clone_backend.model.entity_models.Post;
import ca.sxxxi.twitter_clone_backend.model.form_objects.CommentCreateForm;
import ca.sxxxi.twitter_clone_backend.model.form_objects.PostCreateForm;
import ca.sxxxi.twitter_clone_backend.model.form_objects.PostUpdateForm;
import ca.sxxxi.twitter_clone_backend.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<Optional<Post>> getPostById(@PathVariable UUID pid) {
        return ResponseEntity.ok(postService.getPostByPostId(pid));
    }

    @DeleteMapping("/delete/{pid}")
    public ResponseEntity<?> deletePostById(@PathVariable UUID pid) {
        try {
            postService.deletePostById(pid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody PostCreateForm postForm) {
        Optional<Post> oPost = postService.createPost(postForm);
        return oPost.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @GetMapping("/update/{pid}")
    public ResponseEntity<PostUpdateForm> startPostUpdate(@PathVariable UUID pid) {
        Optional<PostUpdateForm> oPostUpdate = postService.startPostUpdate(pid);
        return oPostUpdate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/update")
    public ResponseEntity<Post> updatePost(@RequestBody PostUpdateForm postUpdate) {
        Optional<Post> oPostUpdate = postService.commitPostUpdate(postUpdate);
        return oPostUpdate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/comment")
    public ResponseEntity<Comment> createComment(@RequestBody CommentCreateForm commentCreateForm) {
        try {
            return postService
                    .postComment(commentCreateForm)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.internalServerError().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
