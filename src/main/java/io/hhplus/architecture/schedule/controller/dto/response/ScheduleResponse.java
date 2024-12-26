package io.hhplus.architecture.schedule.controller.dto.response;

import java.time.LocalDateTime;

public record ScheduleResponse(
        String title,
        String speakerName,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
