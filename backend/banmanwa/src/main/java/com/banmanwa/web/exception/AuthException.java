package com.banmanwa.web.exception;

import org.springframework.http.HttpStatus;

public enum AuthException implements CustomException {
    INVALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED.value()),
    NOT_FOUND_USER("존재하지 않는 사용자입니다.", HttpStatus.UNAUTHORIZED.value());

    private final String message;
    private final int status;

    AuthException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public int getStatus() {
        return 0;
    }
}
