package io.hhplus.architecture.schedule.controller;

import io.hhplus.architecture.schedule.controller.dto.response.ScheduleResponse;
import io.hhplus.architecture.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Value("${grace.period.minutes}")
    private long gracePeriodMinutes;

    @GetMapping("/schedules/{id}")
    public ResponseEntity<List<ScheduleResponse>> findAvailableSchedules(
            @PathVariable Long id
    ) {
        LocalDateTime gracePeriodDate = LocalDateTime.now().plusMinutes(gracePeriodMinutes);
        List<ScheduleResponse> response = scheduleService.findAvailableSchedules(id, gracePeriodDate);
        return ResponseEntity.ok(response);
    }
}
