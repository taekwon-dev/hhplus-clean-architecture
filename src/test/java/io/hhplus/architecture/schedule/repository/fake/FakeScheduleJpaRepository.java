package io.hhplus.architecture.schedule.repository.fake;

import io.hhplus.architecture.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FakeScheduleJpaRepository extends JpaRepository<Schedule, Long> {
}
