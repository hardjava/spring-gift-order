package com.example.demo.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final Key key;
  private static final long ACCESS_TOKEN_EXPIRE_MS = 1000 * 60 * 60;
  private static final long REFRESH_TOKEN_EXPIRE_MS = 1000 * 60 * 60 * 24 * 60;

  public JwtProvider(@Value("${jwt.secret}") String secret) {
    if (secret == null || secret.length() < 32) {
      throw new IllegalStateException("jwt.secret은 32자 이상이어야 합니다.");
    }
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String createAccessToken(Long userId, String email, String role){
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);
    claims.put("email", email);
    claims.put("role", role);
    return createToken(claims, getAccessTokenExpireDate());
  }

  public String createRefreshToken(String email, String role){
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", email);
    claims.put("role", role);
    return createToken(claims, getRefreshTokenExpireDate());
  }

  public Jwt createJwt(Long userId, String email, String role){
    String accessToken = createAccessToken(userId, email, role);
    String refreshToken = createRefreshToken(email, role);
    return new Jwt(accessToken, refreshToken);
  }

  private String createToken(Map<String, Object> claims, Date expiry){
    return Jwts.builder()
               .setClaims(claims)
               .setExpiration(expiry)
               .signWith(key, SignatureAlgorithm.HS256)
               .compact();
  }

  public Date getAccessTokenExpireDate(){
    return new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_MS);
  }

  public Date getRefreshTokenExpireDate(){
    return new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_MS);
  }

  public Claims getClaims(String token){
    try {
      return Jwts.parser()
                 .setSigningKey(key)
                 .build()
                 .parseClaimsJws(token)
                 .getBody();
    } catch (JwtException e){
      throw new IllegalArgumentException("유효하지 않은 JWT입니다.");
    }
  }
}
