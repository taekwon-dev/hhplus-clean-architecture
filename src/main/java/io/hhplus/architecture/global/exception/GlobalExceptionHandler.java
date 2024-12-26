package io.hhplus.architecture.global.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SeminarCustomException.class)
    public ResponseEntity<ProblemDetail> handleCustomException(SeminarCustomException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getStatus(), e.getMessage());
        return ResponseEntity.of(problemDetail).build();
    }
}
