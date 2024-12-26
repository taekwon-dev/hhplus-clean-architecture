package io.hhplus.architecture.schedule.domain;

import io.hhplus.architecture.BaseEntity;
import io.hhplus.architecture.schedule.exception.StartDateAfterEndDateException;
import io.hhplus.architecture.seminar.domain.Seminar;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id")
    private Seminar seminar;

    private String title;

    private Integer maxAttendees;

    private Integer currentAttendees;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    public Schedule(
            Seminar seminar,
            String title,
            Integer maxAttendees,
            Integer currentAttendees,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        validateDate(startDate, endDate);
        this.title = title;
        this.seminar = seminar;
        this.maxAttendees = maxAttendees;
        this.currentAttendees = currentAttendees;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validateDate(LocalDateTime startDate, LocalDateTime endDate) {
        validateStartDateBeforeEndDate(startDate, endDate);
    }

    private void validateStartDateBeforeEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new StartDateAfterEndDateException(startDate, endDate);
        }
    }

    public void incrementCurrentAttendees() {
        this.currentAttendees++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Schedule schedule)) {
            return false;
        }
        return Objects.equals(id, schedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
