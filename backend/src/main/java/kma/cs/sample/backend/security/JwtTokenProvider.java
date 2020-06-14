package kma.cs.sample.backend.security;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kma.cs.sample.backend.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    @Value("${jwt.token.secret}")
    private final String tokenSecret;
    @Value("${jwt.token.validity.duration}")
    private final Duration tokenValidity;

    public String generateToken(final AuthenticatedUser authenticatedUser) {
        return Jwts.builder()
            .setSubject(authenticatedUser.getUsername())
            .setExpiration(getTokenExpiresAt())
            .setIssuedAt(new Date())
            .signWith(SIGNATURE_ALGORITHM, tokenSecret)
            .compact();
    }

    public String extractUserLoginFromToken(final String token) {
        final Claims claims = Jwts.parser()
            .setSigningKey(tokenSecret)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    private Date getTokenExpiresAt() {
        final long now = new Date().getTime();
        return new Date(now + tokenValidity.toMillis());
    }

}
