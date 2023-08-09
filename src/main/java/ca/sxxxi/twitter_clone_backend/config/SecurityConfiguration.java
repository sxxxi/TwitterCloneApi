package ca.sxxxi.twitter_clone_backend.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final AuthenticationProvider authProvider;
    private JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(authProvider)
            .authorizeHttpRequests(request -> {
                request.requestMatchers("/**","/auth/**", "/profile/*", "/profile/following/**", "/profile/followers/**", "/posts/user/**", "/posts/pid/**"
                ).permitAll().anyRequest().authenticated();
            });
        return http.build();
    }
}
