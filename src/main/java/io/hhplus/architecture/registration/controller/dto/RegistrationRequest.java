package io.hhplus.architecture.registration.controller.dto;

import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(

        @NotNull(message = "신청할 스케줄 ID를 입력하세요.")
        Long scheduleId
) {
}
