package com.banmanwa.web.exception;

public class BanManWaException extends RuntimeException {

    private final CustomException customException;

    public BanManWaException(CustomException customException) {
        this.customException = customException;
    }

    public String getMessage() {
        return customException.getMessage();
    }

    public int getStatus() {
        return customException.getStatus();
    }
}
