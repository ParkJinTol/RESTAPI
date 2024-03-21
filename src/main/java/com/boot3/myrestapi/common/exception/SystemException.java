package com.boot3.myrestapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;
    private HttpStatus httpStatus;
    private Throwable throwable;

    public SystemException(Exception e) {
        this(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public SystemException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public SystemException(String message, Throwable t) {
        this.message = message;
        this.throwable =t;
    }

    public SystemException(Throwable t) {
        this.throwable = t;
    }

    public SystemException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
