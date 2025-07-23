package com.example.demo.config;

import com.example.demo.jwt.JwtAuthInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final JwtAuthInterceptor jwtAuthInterceptor;
  private final LoginMemberArgumentResolver loginMemberArgumentResolver;

  public WebConfig(JwtAuthInterceptor jwtAuthInterceptor,
      LoginMemberArgumentResolver loginMemberArgumentResolver) {
    this.jwtAuthInterceptor = jwtAuthInterceptor;
    this.loginMemberArgumentResolver = loginMemberArgumentResolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
    resolvers.add(loginMemberArgumentResolver);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry){
    registry.addInterceptor(jwtAuthInterceptor)
        .addPathPatterns("/users/me");
  }
}
