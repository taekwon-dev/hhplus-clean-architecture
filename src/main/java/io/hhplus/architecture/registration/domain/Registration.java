package io.hhplus.architecture.registration.domain;

import io.hhplus.architecture.BaseEntity;
import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Registration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audience_id")
    private Member audience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Registration(Member audience, Schedule schedule) {
        this.audience = audience;
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Registration registration)) {
            return false;
        }
        return Objects.equals(id, registration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
