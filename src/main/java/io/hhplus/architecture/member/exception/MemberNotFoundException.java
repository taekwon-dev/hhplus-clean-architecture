package io.hhplus.architecture.member.exception;

import io.hhplus.architecture.global.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    private static final String message = "해당 멤버를 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(message);
    }
}
