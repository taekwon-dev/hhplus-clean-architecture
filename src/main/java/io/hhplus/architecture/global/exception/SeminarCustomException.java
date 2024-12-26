package io.hhplus.architecture.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SeminarCustomException extends RuntimeException {

    private final HttpStatus status;

    public SeminarCustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
