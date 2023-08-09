package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.Comment;
import ca.sxxxi.twitter_clone_backend.model.entity_models.Post;
import ca.sxxxi.twitter_clone_backend.model.form_objects.CommentCreateForm;
import ca.sxxxi.twitter_clone_backend.model.form_objects.PostCreateRequest;
import ca.sxxxi.twitter_clone_backend.model.form_objects.PostUpdateRequest;
import ca.sxxxi.twitter_clone_backend.repository.UserRepository;
import ca.sxxxi.twitter_clone_backend.service.CommentsService;
import ca.sxxxi.twitter_clone_backend.service.JwtService;
import ca.sxxxi.twitter_clone_backend.service.PostService;
import ca.sxxxi.twitter_clone_backend.service.ProfileService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostsController {
    private UserRepository userRepository;
    private PostService postService;
    private JwtService jwtService;
    private CommentsService commentsService;

    private Boolean posterIdMatchesClaimUid(String jwt, String username) throws Exception {
        String claimUsername = jwtService.getUsername(jwt);
        return claimUsername.equals(username);
    }

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
    public ResponseEntity<Post> createPost(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody PostCreateRequest postForm) {
        try {
            String username = jwtService.getUsername(auth);
            Optional<Post> oPost = postService.createPost(username, postForm);
            return oPost.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.internalServerError().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/update/{pid}")
    public ResponseEntity<PostUpdateRequest> startPostUpdate(@PathVariable UUID pid) {
        Optional<PostUpdateRequest> oPostUpdate = postService.startPostUpdate(pid);
        return oPostUpdate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/update")
    public ResponseEntity<Post> updatePost(@RequestBody PostUpdateRequest postUpdate) {
        Optional<Post> oPostUpdate = postService.commitPostUpdate(postUpdate);
        return oPostUpdate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }


    @PostMapping("/{postId}/comment")
    public ResponseEntity<UUID> postComment(
            @RequestBody CommentCreateForm commentCreateForm,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String postId
    ) {
        try {
            UserEntity author = userRepository.getUserById(jwtService.getUsername(token))
                    .orElseThrow(() -> new IllegalArgumentException("You do not exist..."));
            UUID recipientId = commentCreateForm.getRecipientId();
            String comment = commentCreateForm.getContent();
            UUID result = commentsService.postComment(UUID.fromString(postId), recipientId, author, comment);
            return ResponseEntity.ok(result);
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<Comment>> getPostComments(
            @RequestParam Integer pageSize,
            @RequestParam Integer page,
            @RequestParam Integer depth,
            @PathVariable String postId
    ) {
        try {
            PageRequest prPageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.DESC, "dateCreated");
            return ResponseEntity.ok(commentsService.getComments(UUID.fromString(postId), depth, prPageRequest).orElseThrow());
        } catch (Exception e) {
            System.err.println(e.toString());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<Page<Comment>> getCommentReplies(
            @PathVariable String commentId,
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam Integer depth
    ) {
        try {
            Comment comment = commentsService.getCommentById(UUID.fromString(commentId));
            PageRequest pageRequest = PageRequest.of(page, pageSize);
            List<Comment> commentReplies = commentsService.getCommentReplies(comment, pageRequest, depth);
            return ResponseEntity.ok(new PageImpl<>(commentReplies, pageRequest, commentReplies.size()));
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }
    }

}
