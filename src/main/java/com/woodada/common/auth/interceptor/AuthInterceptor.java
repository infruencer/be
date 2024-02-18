package com.woodada.common.auth.interceptor;

import com.woodada.common.auth.domain.JwtHandler;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String NULL_AUTH_HEADER = "";
    private final JwtHandler jwtHandler;

    public AuthInterceptor(final JwtHandler jwtHandler/*, final StringRedisTemplate redisTemplate*/) {
        this.jwtHandler = jwtHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String authHeader = getAuthHeader(request);

        try {
            final Long memberId = jwtHandler.extractMemberId(authHeader);
            request.setAttribute("memberId", memberId);
        } catch (ExpiredJwtException e) {
            //todo 리프레시 토큰으로 검증 후 유효하면 엑세스 토큰만 재발급 or AUTH_ERROR
        }

        return true;
    }

    private String getAuthHeader(final HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return Objects.requireNonNullElse(authHeader, NULL_AUTH_HEADER);
    }

}
