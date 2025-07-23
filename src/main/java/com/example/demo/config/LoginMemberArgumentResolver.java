package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.service.user.UserService;
import com.example.demo.validation.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import com.example.demo.jwt.AuthExtractor;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

  private final JwtProvider jwtProvider;
  private final UserService userService;


  public LoginMemberArgumentResolver(JwtProvider jwtProvider, UserService userService) {
    this.jwtProvider = jwtProvider;
    this.userService = userService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginMember.class)
        && parameter.getParameterType().equals(User.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {

    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    return AuthExtractor.extractUserFromRequest(request, jwtProvider, userService);
  }

}
