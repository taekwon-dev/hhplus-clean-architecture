package io.hhplus.architecture.schedule.repository;

import io.hhplus.architecture.schedule.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleCoreRepository implements ScheduleRepository {

    private final ScheduleJpaRepository jpaRepository;

    @Override
    public Schedule save(Schedule schedule) {
        return jpaRepository.save(schedule);
    }

    @Override
    public List<Schedule> findAvailableSchedules(LocalDateTime gracePeriodDate) {
        return jpaRepository.findAllByUserIdAfterGracePeriod(gracePeriodDate);
    }
}
