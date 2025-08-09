package com.zinios.onboard.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ZiniosException extends RuntimeException {
    private final HttpStatus status;

    public ZiniosException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ZiniosException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}