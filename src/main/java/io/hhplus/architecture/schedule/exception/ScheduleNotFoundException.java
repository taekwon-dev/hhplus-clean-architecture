package io.hhplus.architecture.schedule.exception;

import io.hhplus.architecture.global.exception.NotFoundException;

public class ScheduleNotFoundException extends NotFoundException {

    private static final String message = "해당 스케줄을 찾을 수 없습니다.";

    public ScheduleNotFoundException() {
        super(message);
    }
}
