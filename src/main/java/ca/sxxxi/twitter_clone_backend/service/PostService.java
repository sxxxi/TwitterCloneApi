package ca.sxxxi.twitter_clone_backend.service;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.model.Post;
import ca.sxxxi.twitter_clone_backend.model.PostCreate;
import ca.sxxxi.twitter_clone_backend.model.PostUpdate;
import ca.sxxxi.twitter_clone_backend.repository.PostRepository;
import ca.sxxxi.twitter_clone_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepo;
    private UserRepository userRepo;
    private ProfileService profileService;

    public List<Post> getPostByAuthorId(String authorId) {
        return postRepo.getPostsByAuthorId(authorId).stream().map(PostEntity::toModel).collect(Collectors.toList());
    }

    public Optional<Post> getPostByPostId(Long postId) {
        Optional<PostEntity> oPostEntity = postRepo.getPostById(postId);

        if (oPostEntity.isPresent()) {
            // Convert to model before returning
            PostEntity oPost = oPostEntity.get();
            Post p = oPost.toModel();
            p.setAuthor(oPost.getAuthor().toModel());
            return Optional.of(p);
        } else {
            System.out.println("Here");
            return Optional.empty();
        }
    }

    public List<PostEntity> getFeed(String userId) {
        return profileService.getUsersFollowedByUser(userId)
                .stream()
                .map(UserEntity::getId)
                .map(postRepo::getPostsByAuthorId)
                .flatMap(Collection::stream).sorted()
                .collect(Collectors.toList());
    }

    public Optional<PostEntity> createPost(PostCreate postForm) {
        Optional<UserEntity> oUser = userRepo.getUserById(postForm.getPosterId());
        if (oUser.isPresent()) {
            UserEntity user = oUser.get();
            PostEntity postEntity = new PostEntity(
                    postForm.getTitle(),
                    postForm.getContent(),
                    LocalDateTime.now(),
                    user
            );
            postEntity = postRepo.save(postEntity);
            return Optional.of(postEntity);
        }
        return Optional.empty();
    }

    public Optional<PostUpdate> startPostUpdate(Long postId) {
        // Get post then convert to PostUpdate model
        Optional<PostEntity> oPost = postRepo.getPostById(postId);
        if (oPost.isPresent()) {
            PostEntity post = oPost.get();
            return Optional.of(new PostUpdate(post.getId(), post.getTitle(), post.getContent()));
        }
        return Optional.empty();
    }

    public Optional<Post> commitPostUpdate(PostUpdate postUpdate) {
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

    public void deletePostById(Long postId) {
        postRepo.deleteById(postId);
    }
}
