package com.example.demo.jwt;

import com.example.demo.entity.User;
import com.example.demo.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

  private final JwtProvider jwtProvider;
  private final UserService userService;

  public JwtAuthInterceptor(JwtProvider jwtProvider, UserService userService) {
    this.jwtProvider = jwtProvider;
    this.userService = userService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    try {
      User user = AuthExtractor.extractUserFromRequest(request, jwtProvider, userService);

      request.setAttribute("loginUser", user);
      request.setAttribute("userRole", user.getRole());
      return true;
    } catch (Exception e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return false;
    }
  }
}
