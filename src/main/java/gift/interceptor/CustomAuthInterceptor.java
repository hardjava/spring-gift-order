package gift.interceptor;

import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CustomAuthInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public CustomAuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        if (request.getRequestURI().startsWith("/api/products") && request.getMethod()
                .equals("GET")) {
            return true;
        }
        String token = request.getHeader("Authorization");
        Member find = tokenService.isValidateToken(token);
        request.setAttribute("login", find);
        return true;
    }
}
