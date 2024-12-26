package io.hhplus.architecture.schedule.controller.dto.response;

import java.time.LocalDateTime;

public record ScheduleResponse(
        long scheduleId,
        String title,
        String speakerName,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
