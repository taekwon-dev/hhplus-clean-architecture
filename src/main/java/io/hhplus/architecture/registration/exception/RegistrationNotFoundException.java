package io.hhplus.architecture.registration.exception;

import io.hhplus.architecture.global.exception.NotFoundException;

public class RegistrationNotFoundException extends NotFoundException {

    private static final String message = "해당 특강 신청 기록을 찾을 수 없습니다.";

    public RegistrationNotFoundException() {
        super(message);
    }
}
