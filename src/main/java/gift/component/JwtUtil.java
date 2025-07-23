package gift.component;

import gift.enums.Role;
import gift.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public void validateAuthorizationHeader(String authHeader, String realm) {
        validate(authHeader, realm, null);
    }

    public void validateAuthorizationAdminHeader(String authHeader, String realm) {
        validate(authHeader, realm, Role.ROLE_ADMIN);
    }

    private void validate(String authHeader, String realm, Role requiredRole) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization 헤더가 존재하지 않거나 형식이 잘못되었습니다.", realm);
        }

        String token = authHeader.substring(7);
        Claims claims = parseToken(token);

        if (requiredRole != null) {
            String tokenRole = claims.get("role", String.class);
            if (!requiredRole.name().equals(tokenRole)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다. (필요한 권한: " + requiredRole.name() + ")");
            }
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims parseToken(String token) {
        try {

            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다", "gift");
        }
    }

    public Long extractMemberId(String token) {
        Claims claims = parseToken(token);

        return Long.parseLong(claims.get("sub", String.class));
    }

    public String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization 헤더가 존재하지 않거나 형식이 잘못되었습니다.", "gift");
        }

        return authHeader.substring(7);
    }
}
