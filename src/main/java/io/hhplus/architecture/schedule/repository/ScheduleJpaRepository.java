package io.hhplus.architecture.schedule.repository;

import io.hhplus.architecture.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {
}
