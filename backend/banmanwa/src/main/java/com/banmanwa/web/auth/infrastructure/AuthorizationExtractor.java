package com.banmanwa.web.auth.infrastructure;

import java.util.Collections;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

public class AuthorizationExtractor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE =
            AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";
    public static final String BEARER_TYPE = "Bearer";

    public static String extract(HttpServletRequest request) {
        Optional<String> token = findBearerToken(request);
        return token.orElse(null);
    }

    private static Optional<String> findBearerToken(HttpServletRequest request) {
        return Collections.list(request.getHeaders(AUTHORIZATION)).stream()
                .filter(value -> value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))
                .findFirst()
                .map(value -> extractTokenFromHeader(request, value));
    }

    private static String extractTokenFromHeader(HttpServletRequest request, String value) {
        String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
        request.setAttribute(ACCESS_TOKEN_TYPE, value.substring(0, BEARER_TYPE.length()).trim());
        return extractTokenValue(authHeaderValue);
    }

    private static String extractTokenValue(String authHeaderValue) {
        int commaIndex = authHeaderValue.indexOf(",");
        if (commaIndex > 0) {
            return authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;
    }
}
