package io.hhplus.architecture.schedule.repository;

import io.hhplus.architecture.schedule.domain.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    List<Schedule> findAvailableSchedules(long userId, LocalDateTime gracePeriodDate);

    Schedule findById(long id);
}
