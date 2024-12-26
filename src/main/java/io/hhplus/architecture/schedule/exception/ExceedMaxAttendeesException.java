package io.hhplus.architecture.schedule.exception;

import io.hhplus.architecture.global.exception.BadRequestException;

public class ExceedMaxAttendeesException extends BadRequestException {

    private static final String message = "정원을 초과하여 특강을 신청할 수 없습니다.";

    public ExceedMaxAttendeesException() {
        super(message);
    }
}
