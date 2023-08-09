package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.model.entity_models.Post;
import ca.sxxxi.twitter_clone_backend.service.JwtService;
import ca.sxxxi.twitter_clone_backend.service.PostService;
import ca.sxxxi.twitter_clone_backend.service.UserFeedService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed")
@AllArgsConstructor
public class HomeController {
    private PostService postService;
    private UserFeedService userFeedService;
    private JwtService jwtService;

    @GetMapping({"/", ""})
    public ResponseEntity<Page<Post>> getFeedPage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @RequestParam("from") Long from,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("page") Integer page
    ) {
        try {
            String userId = jwtService.getUsername(jwt);
            PageRequest pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.DESC, "dateCreated");
            Page<Post> posts = userFeedService
                    .getFollowedUserPostsBeforeDate(
                            userId,
                            from,
                            pageRequest
                    );
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }
}
