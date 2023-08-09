package ca.sxxxi.twitter_clone_backend.service;

import ca.sxxxi.twitter_clone_backend.entity.comments.CommentEntity;
import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.Comment;
import ca.sxxxi.twitter_clone_backend.model.entity_models.Post;
import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import ca.sxxxi.twitter_clone_backend.model.form_objects.CommentCreateForm;
import ca.sxxxi.twitter_clone_backend.model.form_objects.PostCreateRequest;
import ca.sxxxi.twitter_clone_backend.model.form_objects.PostUpdateRequest;
import ca.sxxxi.twitter_clone_backend.repository.comments.CommentsRepository;
import ca.sxxxi.twitter_clone_backend.repository.PostRepository;
import ca.sxxxi.twitter_clone_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepo;
    private UserRepository userRepo;
    private CommentsRepository commentsRepo;
    private ProfileService profileService;
    private JwtService jwtService;

    public List<Post> getPostByAuthorId(String authorId) {
        return postRepo.getPostsByAuthorId(authorId)
                .stream().map(PostEntity::toModel)
                .collect(Collectors.toList());
    }

    public Optional<Post> getPostByPostId(UUID postId) {
        Optional<PostEntity> oPostEntity = postRepo.getPostById(postId);

        if (oPostEntity.isPresent()) {
            // Convert to model before returning
            PostEntity oPost = oPostEntity.get();
            Post p = oPost.toModel();
            p.setComments(oPost.getComments().stream()
                    .map(CommentEntity::toModel).collect(Collectors.toList()));
            return Optional.of(p);
        } else {
            System.out.println("Here");
            return Optional.empty();
        }
    }

    public List<Post> getFeed(String userId) {
        return profileService.getUsersFollowedByUser(userId)
                .stream()
                .map(User::getId)
                .map(postRepo::getPostsByAuthorId)
                .flatMap(Collection::stream).sorted()
                .map(PostEntity::toModel).collect(Collectors.toList());
    }

    public Optional<Post> createPost(String usernameClaim, PostCreateRequest postForm) {
        Optional<UserEntity> oUser = userRepo.getUserById(usernameClaim);
        if (oUser.isPresent()) {
            UserEntity user = oUser.get();
            PostEntity postEntity = new PostEntity(
                    postForm.getTitle(),
                    postForm.getContent(),
                    user
            );
            postEntity = postRepo.save(postEntity);
            return Optional.of(postEntity.toModel());
        }
        return Optional.empty();
    }

    public Optional<PostUpdateRequest> startPostUpdate(UUID postId) {
        // Get post then convert to PostUpdate model
        Optional<PostEntity> oPost = postRepo.getPostById(postId);
        if (oPost.isPresent()) {
            PostEntity post = oPost.get();
            return Optional.of(new PostUpdateRequest(post.getId(), post.getTitle(), post.getContent()));
        }
        return Optional.empty();
    }

    public Optional<Post> commitPostUpdate(PostUpdateRequest postUpdate) {
        // Make sure post still exists
        Optional<PostEntity> oPost = postRepo.getPostById(postUpdate.getId());
        if (oPost.isPresent()) {
            PostEntity post = oPost.get();
            post.setTitle(postUpdate.getTitle());
            post.setContent(postUpdate.getContent());
            post = postRepo.save(post);
            return Optional.of(post.toModel());
        }
        return Optional.empty();
    }

    public void deletePostById(UUID postId) {
        postRepo.deleteById(postId);
    }
}
