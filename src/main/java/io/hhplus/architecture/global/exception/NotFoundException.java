package io.hhplus.architecture.global.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends SeminarCustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public NotFoundException(String message) {
        super(message, STATUS);
    }
}
