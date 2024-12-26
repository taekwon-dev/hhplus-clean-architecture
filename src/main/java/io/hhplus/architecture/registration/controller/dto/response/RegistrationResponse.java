package io.hhplus.architecture.registration.controller.dto.response;

import java.time.LocalDateTime;

public record RegistrationResponse(
        long scheduleId,
        String title,
        String speakerName,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
