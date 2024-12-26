package io.hhplus.architecture.registration.domain;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.seminar.domain.Seminar;
import io.hhplus.architecture.util.fixture.MemberFixture;
import io.hhplus.architecture.util.fixture.ScheduleFixture;
import io.hhplus.architecture.util.fixture.SeminarFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;

class RegistrationTest {

    @DisplayName("특강 신청 기록을 생성한다.")
    @Test
    void createSeminarRegistration() {
        // given
        Member audience = MemberFixture.AUDIENCE();
        Seminar seminar = SeminarFixture.create(MemberFixture.SPEAKER());
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);
        Schedule schedule = ScheduleFixture.create(seminar, startDate, endDate);

        // when & then
        assertThatCode(() -> new Registration(audience, schedule))
                .doesNotThrowAnyException();
    }
}
