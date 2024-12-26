package io.hhplus.architecture.schedule.service.mapper;

import io.hhplus.architecture.schedule.controller.dto.response.ScheduleResponse;
import io.hhplus.architecture.schedule.domain.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleMapper {

    public List<ScheduleResponse> mapToScheduleResponse(List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> new ScheduleResponse(
                        schedule.getTitle(),
                        schedule.getSpeakerName(),
                        schedule.getDescription(),
                        schedule.getStartDate(),
                        schedule.getEndDate()
                )).toList();
    }
}
