package io.hhplus.architecture.registration.controller.dto.response;

import java.time.LocalDateTime;

public record RegistrationResponse(
        String title,
        String speakerName,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
