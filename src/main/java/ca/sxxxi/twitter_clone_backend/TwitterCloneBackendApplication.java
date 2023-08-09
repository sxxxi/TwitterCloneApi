package ca.sxxxi.twitter_clone_backend;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.repository.PostRepository;
import ca.sxxxi.twitter_clone_backend.repository.UserRepository;
import ca.sxxxi.twitter_clone_backend.service.CommentsService;
import ca.sxxxi.twitter_clone_backend.service.ProfileService;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@AllArgsConstructor
public class TwitterCloneBackendApplication {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private PasswordEncoder passwordEncoder;
    private ProfileService profileService;
    private CommentsService commentsService;

    public static void main(String[] args) {
        SpringApplication.run(TwitterCloneBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner populateDb() {
        return args -> {
            var sxxxi = new UserEntity("sxxxi", "", "Seiji", "Akakabe", passwordEncoder.encode("foobar"));
            var seajay = new UserEntity("seejay", "", "Johnny", "Bingo", passwordEncoder.encode("foobar"));

            userRepository.saveAll(List.of(
                    sxxxi, seajay
            ));

            PostEntity trendy = new PostEntity("Trending post!", "Thank you for the support, guys!", seajay);
            postRepository.save(trendy);

            for (int i = 0; i < 1; i++) {
                var top1 = commentsService.postComment(trendy.getId(), null, sxxxi, "Good job Mr. Bingo!");
                var top1reply1 = commentsService.postComment(trendy.getId(), top1, seajay, "Appreciate it :)");
                commentsService.postComment(trendy.getId(), top1reply1, sxxxi, "Bingbong!");
                var top1reply2 = commentsService.postComment(trendy.getId(), top1, sxxxi, "Thanks for the Titter gold kind stranger! *tips fedora*");
                commentsService.postComment(trendy.getId(), top1reply2, seajay, "What a kind peron!");
                commentsService.postComment(trendy.getId(), top1reply2, sxxxi, "Schizo");
            }

            profileService.followUser(sxxxi.getId(), seajay.getId());

        };
    }

}
