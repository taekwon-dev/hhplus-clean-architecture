package io.hhplus.architecture.schedule.repository;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.seminar.domain.Seminar;
import io.hhplus.architecture.seminar.repository.SeminarRepository;
import io.hhplus.architecture.util.RepositoryTest;
import io.hhplus.architecture.util.fixture.MemberFixture;
import io.hhplus.architecture.util.fixture.ScheduleFixture;
import io.hhplus.architecture.util.fixture.SeminarFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SeminarRepository seminarRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("특강 스케줄을 저장한다.")
    @Test
    void saveSeminarSchedule() {
        //given
        Member speaker = MemberFixture.SPEAKER();
        Member savedSpeaker = memberRepository.save(speaker);

        Seminar seminar = SeminarFixture.create(savedSpeaker);
        Seminar savedSeminar = seminarRepository.save(seminar);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);
        Schedule schedule = ScheduleFixture.create(savedSeminar, startDate, endDate);

        //when
        Schedule savedSchedule = scheduleRepository.save(schedule);

        //then
        assertThat(savedSchedule.getTitle()).isEqualTo(schedule.getTitle());
        assertThat(savedSchedule.getMaxAttendees()).isEqualTo(schedule.getMaxAttendees());
        assertThat(savedSchedule.getCurrentAttendees()).isEqualTo(schedule.getCurrentAttendees());
        assertThat(savedSchedule.getStartDate()).isEqualTo(schedule.getStartDate());
        assertThat(savedSchedule.getEndDate()).isEqualTo(schedule.getEndDate());
    }
}
