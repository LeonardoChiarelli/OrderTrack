package br.com.OrderTrack.Common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String ISSUER = "ORDER TRACK API";

    @Value("${app.security.token.secret}")
    private String tokenSecret;

    public String generateToken(UserPrincipal user) {
        try {
            var algorithm = Algorithm.HMAC256(tokenSecret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.email())
                    .withClaim("id", user.id().toString())
                    .withClaim("name", user.name())
                    .withClaim("roles", user.roles())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error generating JWT Token", exception);
        }
    }

    public DecodedJWT validateAndDecode(String token) {
        try {
            var algorithm = Algorithm.HMAC256(tokenSecret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token invalid or expired");
        }
    }

    public String getSubject(String token) {
        return validateAndDecode(token).getSubject();
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
    }
}