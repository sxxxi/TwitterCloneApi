package ca.sxxxi.twitter_clone_backend.service;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.repository.PostRepository;
import ca.sxxxi.twitter_clone_backend.repository.comments.CommentReplyRepository;
import ca.sxxxi.twitter_clone_backend.repository.comments.CommentsRepository;
import ca.sxxxi.twitter_clone_backend.repository.comments.PostReplyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestConfiguration
class CommentsServiceConfig {
    @Bean
    PostRepository postRepository() {
        return Mockito.mock(PostRepository.class);
    }
    @Bean
    PostReplyRepository postReplyRepository() {
        return Mockito.mock(PostReplyRepository.class);
    }
    @Bean
    CommentReplyRepository commentReplyRepository() {
        return Mockito.mock(CommentReplyRepository.class);
    }

    @Bean
    CommentsRepository commentsRepository() {
        return Mockito.mock(CommentsRepository.class);
    }

    @Bean
    public CommentsService testCommentsService(
            PostRepository postRepository,
            PostReplyRepository postReplyRepository,
            CommentReplyRepository commentReplyRepository,
            CommentsRepository commentsRepository
    ) {
        return new CommentsService(postRepository, postReplyRepository, commentReplyRepository, commentsRepository);
    }
}

@Import(CommentsServiceConfig.class)
@SpringBootTest(classes = { CommentsService.class })
class CommentsServiceTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostReplyRepository postReplyRepo;
    @Autowired
    private CommentsService commentsService;
    private final UUID randomUUID = UUID.randomUUID();

    @ParameterizedTest
    @ValueSource(strings = { "5d49fb93-b1f4-4eac-ba21-ac68529a3595", "" })
    public void CommentsService_getComments_passingIllegalPostIdThrowsException(String value) {
        UUID nonExistentPost;
        if (value.isEmpty()) {
            nonExistentPost = null;
        }
        else {
            nonExistentPost = UUID.fromString(value);
            // Emulate not finding existing post
            Mockito.when(postRepository.existsById(nonExistentPost)).thenReturn(false);
        }

        assertThrows(
            IllegalArgumentException.class,
            () -> commentsService.getComments(nonExistentPost, 1, PageRequest.of(0, 3))
        );
    }

    @Test
    public void CommentsService_getComments_exceedingMaxDepthThrowsException() {
        Mockito.when(postRepository.existsById(randomUUID)).thenReturn(true);
        assertThrows(
                IllegalArgumentException.class,
                () -> commentsService.getComments(randomUUID, 9, PageRequest.of(0, 3))
        );
    }

    @Test
    public void CommentsService_getComments_boundaryDepthSizeSucceeds() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        Mockito.when(postRepository.existsById(randomUUID)).thenReturn(true);
        Mockito.when(postReplyRepo.getPostReplyEntitiesByPostOwner_Id(randomUUID, pageRequest)).thenReturn(Page.empty());
        assertDoesNotThrow(() -> commentsService.getComments(randomUUID, 8, pageRequest));
    }

    @Test
    public void CommentService_postComment_postMustBePresentWhenPostingTopLevelComment() {
        Mockito.when(postRepository.getPostById(randomUUID)).thenReturn(Optional.empty());
        var author = new UserEntity();
        assertThrows(IllegalArgumentException.class, () -> commentsService.postComment(randomUUID, null, author, "This is not good"));
    }

    @Test
    public void CommentService_postComment_commentMustNotBeEmpty() {
        Mockito.when(postRepository.getPostById(randomUUID)).thenReturn(Optional.of(new PostEntity()));
        var author = new UserEntity();
        assertThrows(IllegalArgumentException.class, () -> commentsService.postComment(randomUUID, null, author, "  "));
    }
}