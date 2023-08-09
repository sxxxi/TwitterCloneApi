package ca.sxxxi.twitter_clone_backend.service;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import ca.sxxxi.twitter_clone_backend.model.form_objects.AuthenticationResponse;
import ca.sxxxi.twitter_clone_backend.model.form_objects.LoginRequest;
import ca.sxxxi.twitter_clone_backend.model.form_objects.SignupRequest;
import ca.sxxxi.twitter_clone_backend.repository.UserRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AuthenticationService {
    private UserRepository userRepo;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationProvider authProvider;

    public String signup(SignupRequest req) throws JWTVerificationException {

        // Initialize entity to insert
        UserEntity newUser = new UserEntity();
        newUser.setId(req.getUsername());
        newUser.setFirstName(req.getFirstName());
        newUser.setLastName(req.getLastName());
        newUser.setHash(passwordEncoder.encode(req.getPassword()));
        newUser.setPfp(req.getPfp());

        if (userRepo.existsById(newUser.getId())) {
            throw new IllegalArgumentException("User already exists");
        }

        // Generate token
        String token = jwtService.issueToken(newUser.getUsername(), newUser.getFirstName(), newUser.getLastName());

        // Insert when token generation is successful
        userRepo.save(newUser);
        return token;
    }

    public String login(LoginRequest request) throws AuthenticationException, UnexpectedException {
        // This should fail if user doesn't exist
        authProvider.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        return userRepo.getUserById(request.getUsername())
                .map(u -> jwtService.issueToken(u.getUsername(), u.getFirstName(), u.getLastName()))
                .orElseThrow(() -> new UnexpectedException("WHy?"));
    }


    public void createNewUser(UserEntity user) {
        userRepo.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepo.getUserById(id).map(UserEntity::toModel);
    }



}
