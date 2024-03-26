package test.jet.game.infrastructure.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import test.jet.game.domain.services.ITokenService;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class TokenService implements ITokenService {

    // Should inject as an environment variable in production use cases
    private final String SECRET_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";
    @Override
    public String sign(String email) {
        return Jwts.builder().setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Boolean isTokenValid(String token, String email) {
        String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return this.extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Key getSignKey () {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }
}
