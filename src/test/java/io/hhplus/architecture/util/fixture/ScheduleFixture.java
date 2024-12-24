package io.hhplus.architecture.util.fixture;

import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.seminar.domain.Seminar;

import java.time.LocalDateTime;

public class ScheduleFixture {

    public static Schedule create(Seminar seminar, LocalDateTime startDate, LocalDateTime endDate) {
        return new Schedule(seminar, "TDD 특강", 30, 0, startDate, endDate);
    }
}
