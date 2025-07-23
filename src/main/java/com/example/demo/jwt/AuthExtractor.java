package com.example.demo.jwt;

import com.example.demo.entity.User;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.service.user.UserService;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;

public class AuthExtractor {

  public static User extractUserFromRequest(HttpServletRequest request, JwtProvider jwtProvider,
      UserService userService) {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnauthorizedException("AccessToken이 없습니다.");
    }

    String token = authHeader.replace("Bearer ", "").trim();
    String email = extractEmailFromToken(token, jwtProvider);
    return userService.findByEmail(email);
  }

  private static String extractEmailFromToken(String token, JwtProvider jwtProvider) {
    try {
      return jwtProvider.getClaims(token).get("email", String.class);
    } catch (Exception e) {
      throw new UnauthorizedException("AccessToken이 유효하지 않습니다.");
    }
  }
}
