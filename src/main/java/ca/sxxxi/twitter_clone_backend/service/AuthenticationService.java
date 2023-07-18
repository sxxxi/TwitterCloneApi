package ca.sxxxi.twitter_clone_backend.service;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import ca.sxxxi.twitter_clone_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class AuthenticationService {
    private UserRepository userRepo;

    public void createNewUser(UserEntity user) {
        userRepo.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepo.getUserById(id).map(UserEntity::toModel);
    }



}
