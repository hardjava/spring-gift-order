package gift.config;

import gift.interceptor.CustomAuthInterceptor;
import gift.resolver.LoginMemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CustomAuthInterceptor customAuthInterceptor;

    public CustomConfig(
            LoginMemberArgumentResolver loginMemberArgumentResolver,
            CustomAuthInterceptor customAuthInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.customAuthInterceptor = customAuthInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/members/register", "/api/members/login",
                        "/admin/boards/**");
    }
}