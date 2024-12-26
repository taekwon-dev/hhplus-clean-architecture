package io.hhplus.architecture.schedule.repository;

import io.hhplus.architecture.schedule.domain.Schedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {

    @Query("""
            SELECT s FROM Schedule s
            LEFT JOIN Registration r ON r.schedule.id = s.id AND r.audience.id =:userId
            WHERE s.startDate >= :gracePeriodDate AND s.maxAttendees > s.currentAttendees AND r IS NULL
            ORDER BY s.startDate DESC, s.id DESC
            """)
    List<Schedule> findAllByUserIdAfterGracePeriod(long userId, LocalDateTime gracePeriodDate);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Schedule> findById(long id);
}
