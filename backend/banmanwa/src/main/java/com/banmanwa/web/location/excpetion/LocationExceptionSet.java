package com.banmanwa.web.location.excpetion;

import com.banmanwa.web.exception.CustomException;
import org.springframework.http.HttpStatus;

public enum LocationExceptionSet implements CustomException {

    INVALID_LOCATION("위치를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    LocationExceptionSet(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return status;
    }
}
