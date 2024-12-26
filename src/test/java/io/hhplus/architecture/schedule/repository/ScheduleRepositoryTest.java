package io.hhplus.architecture.schedule.repository;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.schedule.exception.ScheduleNotFoundException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);
        Schedule schedule = ScheduleFixture.create(seminar, startDate, endDate);

        //when
        Schedule savedSchedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));

        //then
        assertThat(savedSchedule.getTitle()).isEqualTo(schedule.getTitle());
        assertThat(savedSchedule.getMaxAttendees()).isEqualTo(schedule.getMaxAttendees());
        assertThat(savedSchedule.getCurrentAttendees()).isEqualTo(schedule.getCurrentAttendees());
        assertThat(savedSchedule.getStartDate()).isEqualTo(schedule.getStartDate());
        assertThat(savedSchedule.getEndDate()).isEqualTo(schedule.getEndDate());
    }

    @DisplayName("ID를 기반으로 스케줄를 조회한다.")
    @Test
    void findScheduleById() {
        // given
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);
        Schedule schedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));

        // when
        Schedule findSchedule = scheduleRepository.findById(schedule.getId());

        // then
        assertThat(findSchedule.getTitle()).isEqualTo(schedule.getTitle());
        assertThat(findSchedule.getMaxAttendees()).isEqualTo(schedule.getMaxAttendees());
        assertThat(findSchedule.getCurrentAttendees()).isEqualTo(schedule.getCurrentAttendees());
        assertThat(findSchedule.getStartDate()).isEqualTo(schedule.getStartDate());
        assertThat(findSchedule.getEndDate()).isEqualTo(schedule.getEndDate());
    }

    @DisplayName("타겟 스케줄 ID가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void findScheduleById_WhenScheduleNotExist() {
        // given
        long notExistScheduleId = 1L;

        // when & then
        assertThatThrownBy(() -> scheduleRepository.findById(notExistScheduleId)).isInstanceOf(ScheduleNotFoundException.class);
    }
}
