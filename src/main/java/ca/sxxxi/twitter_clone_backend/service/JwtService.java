package ca.sxxxi.twitter_clone_backend.service;

import ca.sxxxi.twitter_clone_backend.model.AuthToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private Algorithm algorithm;
    private JWTVerifier verifier;


    // TODO: Function that retrieves claims as strings.

    public String issueToken(String username, String firstName, String lastName) {
        Date currentDate = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 7);     // Expires in a week
        Date expirationDate = calendar.getTime();

        return new AuthToken.Builder(algorithm)
                .setName(firstName, lastName)
                .setUsername(username)
                .setIssuedDate(currentDate)
                .setIssuer("twitter_clone")
                .setExpiryDate(expirationDate)
                .build().toJwt();
    }

    public AuthToken.Decoder getDecodedAuthToken(String authToken) {
        return new AuthToken.Decoder(authToken, verifier);
    }

    public String getUsername(String authToken) throws IllegalArgumentException {
        return getDecodedAuthToken(authToken).getUsername().orElseThrow(() -> new IllegalArgumentException("Authorization token not properly set"));
    }

    public DecodedJWT validateToken(String token) {
        return verifier.verify(token);
    }

}
