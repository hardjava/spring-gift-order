package gift.security.config;

import gift.member.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final String secretKey;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3_600_000);

        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .claim("role", member.getRole().name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long extractMemberId(String token) {
        String subject = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return Long.parseLong(subject);
    }
}
