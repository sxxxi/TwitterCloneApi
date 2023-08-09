package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.entity.UserEntity;
import ca.sxxxi.twitter_clone_backend.model.form_objects.AuthenticationResponse;
import ca.sxxxi.twitter_clone_backend.model.form_objects.LoginRequest;
import ca.sxxxi.twitter_clone_backend.model.entity_models.User;
import ca.sxxxi.twitter_clone_backend.model.form_objects.SignupRequest;
import ca.sxxxi.twitter_clone_backend.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.UnexpectedException;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthenticationService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody SignupRequest request) {
        try {
            return ResponseEntity.ok(new AuthenticationResponse(authService.signup(request)));
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(new AuthenticationResponse(authService.login(loginRequest)));
        } catch (UnexpectedException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<?> unregister() {
        return ResponseEntity.ok().build();
    }
}
