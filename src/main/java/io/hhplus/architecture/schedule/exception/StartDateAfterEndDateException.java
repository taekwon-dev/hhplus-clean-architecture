package io.hhplus.architecture.schedule.exception;

import io.hhplus.architecture.global.exception.BadRequestException;

import java.time.LocalDateTime;

public class StartDateAfterEndDateException extends BadRequestException {

    private static final String message = "특강의 시작 시간 (%s)은 종료 시간 (%s)보다 늦을 수 없습니다.";

    public StartDateAfterEndDateException(LocalDateTime startDate, LocalDateTime endDate) {
        super(String.format(message, startDate, endDate));
    }
}
