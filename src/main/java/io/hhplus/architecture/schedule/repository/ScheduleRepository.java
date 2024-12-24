package io.hhplus.architecture.schedule.repository;

import io.hhplus.architecture.schedule.domain.Schedule;

public interface ScheduleRepository {

    Schedule save(Schedule schedule);
}
