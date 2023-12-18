package com.banmanwa.web;

import com.banmanwa.web.exception.BanManWaException;
import com.banmanwa.web.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BanManWaException.class)
    public ResponseEntity<ErrorResponse> handleBanManWaException(BanManWaException e) {
        return ResponseEntity.status(e.getStatus())
            .body(new ErrorResponse(e.getMessage()));
    }
}
