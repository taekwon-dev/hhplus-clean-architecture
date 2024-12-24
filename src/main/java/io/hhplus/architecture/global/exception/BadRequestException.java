package io.hhplus.architecture.global.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends SeminarCustomException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(message, STATUS);
    }
}
