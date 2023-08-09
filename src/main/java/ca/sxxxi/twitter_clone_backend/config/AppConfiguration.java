package ca.sxxxi.twitter_clone_backend.config;

import ca.sxxxi.twitter_clone_backend.service.CustomUserDetailsService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class AppConfiguration {
    private final CustomUserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder, UserDetailsService userDetailsService
    ) {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider(passwordEncoder);
        dao.setUserDetailsService(userDetailsService);
        return dao;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public Algorithm jwtAlgo() {
        // TODO: Move key to env
        return Algorithm.HMAC384("wqzeQSXMMTIuybKY5kEG6SSYAspTCriewW02Mx5aery1O5QjGKuGveDO/CM3cLzHEVieuuOBk6dUvtSVjs+QfuyZbvV7v6z6vTTzGreD/fY=");
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).withIssuer("twitter_clone").build();
    }

}
