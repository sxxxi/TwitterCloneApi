package ca.sxxxi.twitter_clone_backend.model;

import ca.sxxxi.twitter_clone_backend.service.JwtService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class AuthToken {
    private static final Algorithm algorithm = Algorithm.HMAC384("wqzeQSXMMTIuybKY5kEG6SSYAspTCriewW02Mx5aery1O5QjGKuGveDO/CM3cLzHEVieuuOBk6dUvtSVjs+QfuyZbvV7v6z6vTTzGreD/fY=");
    private String jwt;
    private static final String USERNAME_KEY = "username";
    private static final String FIRSTNAME_KEY = "firstName";
    private static final String LASTNAME_KEY = "lastName";

    private AuthToken() {}
    private AuthToken(String jwt) {
        this.jwt = jwt;
    }

    public static Builder builder() {
        return new Builder(algorithm);
    }

    public static class Builder {
        private final Algorithm algorithm;
        private final JWTCreator.Builder jws = JWT.create();

        public Builder(Algorithm algorithm) {
            this.algorithm = algorithm;
        }

        public Builder setUsername(String username) {
            jws.withClaim("username", username);
            return this;
        }

        public Builder setName(String firstName, String lastName) {
            jws.withClaim("firstName", firstName);
            jws.withClaim("lastName", lastName);
            return this;
        }

        public Builder setIssuer(String issuer) {
            jws.withIssuer(issuer);
            return this;
        }

        public Builder setIssuedDate(Date issuedDate) {
            jws.withIssuedAt(issuedDate);
            return this;
        }

        public Builder setExpiryDate(Date exp) {
            jws.withExpiresAt(exp);
            return this;
        }

        public AuthToken build() {
            return new AuthToken(jws.sign(algorithm));
        }
    }


    public static class Decoder {
        private final DecodedJWT decoded;
        public Decoder(String jwt, JWTVerifier verifier) {
            // string can be purely jwt or the whole auth header
            List<String> sp = Arrays.stream(jwt.split(" ")).toList();
            if (sp.size() == 1) {
                jwt = sp.get(0);
            } else if (sp.size() == 2 && sp.get(0).equals("Bearer")) {
                jwt = sp.get(1);
            } else {
                throw new IllegalArgumentException("Jwt string can only be the jwt or the auth header");
            }
            decoded = verifier.verify(jwt);
        }

        public Optional<String> getUsername() {
            return getClaim(USERNAME_KEY);
        }

        public Optional<String> getFirstName() {
            return getClaim(FIRSTNAME_KEY);
        }

        public Optional<String> getLastName() {
            return getClaim(LASTNAME_KEY);
        }

        private Optional<String> getClaim(String claim) {
            Claim c = decoded.getClaim(claim);
            if (c.isMissing()) {
                return Optional.empty();
            }
            return Optional.of(c.asString());
        }

    }

    public String toJwt() {
        return jwt;
    }
}
