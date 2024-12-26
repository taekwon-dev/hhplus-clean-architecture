package io.hhplus.architecture.schedule.domain;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.schedule.exception.StartDateAfterEndDateException;
import io.hhplus.architecture.seminar.domain.Seminar;
import io.hhplus.architecture.util.fixture.MemberFixture;
import io.hhplus.architecture.util.fixture.SeminarFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleTest {

    @DisplayName("특강 스케줄을 생성한다.")
    @Test
    void createSeminarSchedule() {
        // given
        Member speaker = MemberFixture.SPEAKER();
        Seminar seminar = SeminarFixture.create(speaker);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);
        String title = "TDD 특강";
        int maxAttendees = 30;
        int currentAttendees = 0;

        // when & then
        assertThatCode(() -> new Schedule(seminar, title, speaker.getName(), seminar.getDescription(), maxAttendees, currentAttendees, startDate, endDate))
                .doesNotThrowAnyException();
    }

    @DisplayName("특강 시작 시간이 종료 시간보다 늦은 경우 예외가 발생한다.")
    @Test
    void invalidSeminarDate() {
        // given
        Member speaker = MemberFixture.SPEAKER();
        Seminar seminar = SeminarFixture.create(speaker);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.minusHours(1);
        String title = "TDD 특강";
        int maxAttendees = 30;
        int currentAttendees = 0;

        // when & then
        assertThatThrownBy(() -> new Schedule(seminar, title, speaker.getName(), seminar.getDescription(), maxAttendees, currentAttendees, startDate, endDate))
                .isInstanceOf(StartDateAfterEndDateException.class);
    }
}
