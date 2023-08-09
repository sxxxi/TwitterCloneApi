package ca.sxxxi.twitter_clone_backend.config;

import ca.sxxxi.twitter_clone_backend.service.CustomUserDetailsService;
import ca.sxxxi.twitter_clone_backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService userDetailsService;
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get bearer token
        String authHeader = request.getHeader("Authorization");

        // Make sure auth header is not null
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtService.getDecodedAuthToken(authHeader).getUsername().orElse(null);
        if (username == null || username.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get the user of username from db and authenticate
        // Do this only if not authenticated yet
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
