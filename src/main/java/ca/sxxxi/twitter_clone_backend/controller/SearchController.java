package ca.sxxxi.twitter_clone_backend.controller;

import ca.sxxxi.twitter_clone_backend.model.response_models.UserSearchResult;
import ca.sxxxi.twitter_clone_backend.service.AuthenticationService;
import ca.sxxxi.twitter_clone_backend.service.JwtService;
import ca.sxxxi.twitter_clone_backend.service.SearchService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
    private JwtService jwtService;
    private SearchService searchService;

    @GetMapping("/user/{searchQuery}")
    public ResponseEntity<Page<UserSearchResult>> findUsersLike(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @PathVariable String searchQuery,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("page") Integer page
    ) {
        try {
            String username = jwtService.getUsername(jwt);
            Pageable pageRequest = PageRequest.of(page, pageSize);
            return ResponseEntity.ok(
                    searchService.findUserWithUsernameOrName(username, searchQuery, pageRequest)
            );
        } catch (JWTVerificationException e) {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }
}
