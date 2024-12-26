package io.hhplus.architecture.registration.exception;

import io.hhplus.architecture.global.exception.BadRequestException;

public class InvalidScheduleException extends BadRequestException {

    private static final String message = "신청 시점으로부터 30분 이내에 시작하는 스케줄은 신청할 수 없습니다.";

    public InvalidScheduleException() {
        super(message);
    }
}
