package io.hhplus.architecture.schedule.repository.fake;

import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.schedule.exception.ScheduleNotFoundException;
import io.hhplus.architecture.schedule.repository.ScheduleRepository;

import java.time.LocalDateTime;
import java.util.List;

public class FakeScheduleCoreRepository implements ScheduleRepository {

    private final FakeScheduleJpaRepository fakeJpaRepository;

    public FakeScheduleCoreRepository(FakeScheduleJpaRepository fakeJpaRepository) {
        this.fakeJpaRepository = fakeJpaRepository;
    }

    @Override
    public Schedule save(Schedule schedule) {
        return fakeJpaRepository.save(schedule);
    }

    @Override
    public List<Schedule> findAvailableSchedules(long userId, LocalDateTime gracePeriodDate) {
        return List.of();
    }

    @Override
    public Schedule findById(long id) {
        return fakeJpaRepository.findById(id).orElseThrow(ScheduleNotFoundException::new);
    }
}
