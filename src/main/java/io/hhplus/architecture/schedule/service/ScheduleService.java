package io.hhplus.architecture.schedule.service;

import io.hhplus.architecture.schedule.controller.dto.response.ScheduleResponse;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.schedule.repository.ScheduleRepository;
import io.hhplus.architecture.schedule.service.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAvailableSchedules(LocalDateTime gracePeriodDate) {
        List<Schedule> schedules = scheduleRepository.findAvailableSchedules(gracePeriodDate);
        return scheduleMapper.mapToScheduleResponse(schedules);
    }
}
