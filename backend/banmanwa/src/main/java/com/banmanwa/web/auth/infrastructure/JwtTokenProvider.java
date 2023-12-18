package com.banmanwa.web.auth.infrastructure;

import com.banmanwa.web.secret.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + SecretKey.JWT_EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SecretKey.JWT_SECRET_KEY)
                .compact();
    }

    public String extractId(String token) {
        validateToken(token);
        return Jwts.parser().setSigningKey(SecretKey.JWT_SECRET_KEY).parseClaimsJws(token).getBody()
                .getSubject();
    }

    private boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SecretKey.JWT_SECRET_KEY)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new RuntimeException();
        }
    }
}
