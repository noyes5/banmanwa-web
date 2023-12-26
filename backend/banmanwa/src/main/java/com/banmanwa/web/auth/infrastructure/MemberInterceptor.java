package com.banmanwa.web.auth.infrastructure;

import com.banmanwa.web.auth.service.AuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class MemberInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public MemberInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        String token = AuthorizationExtractor.extract(request);
        authService.validate(token);

        return true;
    }
}
