package io.hhplus.architecture.registration.exception;

import io.hhplus.architecture.global.exception.BadRequestException;

public class AlreadyRegisteredException extends BadRequestException {

    private static final String message = "이미 신청 완료된 특강입니다.";

    public AlreadyRegisteredException() {
        super(message);
    }
}
