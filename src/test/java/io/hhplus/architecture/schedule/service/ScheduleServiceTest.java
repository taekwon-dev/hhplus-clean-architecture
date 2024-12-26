package io.hhplus.architecture.schedule.service;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.registration.controller.dto.request.RegistrationRequest;
import io.hhplus.architecture.registration.service.RegistrationService;
import io.hhplus.architecture.schedule.controller.dto.response.ScheduleResponse;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.schedule.repository.ScheduleRepository;
import io.hhplus.architecture.seminar.domain.Seminar;
import io.hhplus.architecture.seminar.repository.SeminarRepository;
import io.hhplus.architecture.util.ServiceTest;
import io.hhplus.architecture.util.fixture.MemberFixture;
import io.hhplus.architecture.util.fixture.ScheduleFixture;
import io.hhplus.architecture.util.fixture.SeminarFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleServiceTest extends ServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SeminarRepository seminarRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ScheduleService scheduleService;

    @DisplayName("예약 가능한 특강 스케줄을 조회한다.")
    @Test
    void findAvailableSchedules() {
        // given
        Member audience = memberRepository.save(MemberFixture.AUDIENCE());
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusHours(1);
        LocalDateTime endDate = startDate.plusHours(2);
        Schedule schedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));

        // when
        List<ScheduleResponse> findAvailableSchedules = scheduleService.findAvailableSchedules(audience.getId(), currentDate);

        // then
        assertThat(findAvailableSchedules.size()).isOne();
        assertThat(findAvailableSchedules.get(0).scheduleId()).isEqualTo(schedule.getId());
        assertThat(findAvailableSchedules.get(0).title()).isEqualTo(schedule.getTitle());
        assertThat(findAvailableSchedules.get(0).speakerName()).isEqualTo(schedule.getSeminar().getSpeaker().getName());
        assertThat(findAvailableSchedules.get(0).description()).isEqualTo(schedule.getSeminar().getDescription());
        assertThat(findAvailableSchedules.get(0).startDate()).isEqualTo(schedule.getStartDate());
        assertThat(findAvailableSchedules.get(0).endDate()).isEqualTo(schedule.getEndDate());
    }

    @DisplayName("정원이 가득찬 특강 스케줄의 경우, 예약이 불가능하다.")
    @Test
    void findAvailableSchedules_WhenFullyApplied() {
        // given
        Member audience = memberRepository.save(MemberFixture.AUDIENCE());
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusHours(1);
        LocalDateTime endDate = startDate.plusHours(2);
        scheduleRepository.save(ScheduleFixture.createWithMaxAttendees(seminar, startDate, endDate));

        // when
        List<ScheduleResponse> findAvailableSchedules = scheduleService.findAvailableSchedules(audience.getId(), currentDate);

        // then
        assertThat(findAvailableSchedules.size()).isZero();
    }

    @DisplayName("예약 가능 시간 범위에 특강 스케줄이 없는 경우, 예약이 불가능하다.")
    @Test
    void findAvailableSchedules_WhenAllBeforeGracePeriodDate() {
        // given
        Member audience = memberRepository.save(MemberFixture.AUDIENCE());
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.minusHours(1);
        LocalDateTime endDate = startDate.plusHours(2);
        scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));

        // when
        List<ScheduleResponse> findAvailableSchedules = scheduleService.findAvailableSchedules(audience.getId(), currentDate);

        // then
        assertThat(findAvailableSchedules.size()).isZero();
    }

    @DisplayName("이미 예약 완료한 스케줄은 예약이 불가능하다.")
    @Test
    void findAvailableSchedules_WhenAlreadyRegisteredSchedule() {
        // given
        Member audience = memberRepository.save(MemberFixture.AUDIENCE());
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusHours(1);
        LocalDateTime endDate = startDate.plusHours(2);
        Schedule schedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));
        registrationService.registerSchedule(audience.getId(), new RegistrationRequest(schedule.getId()), LocalDateTime.now());

        // when
        List<ScheduleResponse> findAvailableSchedules = scheduleService.findAvailableSchedules(audience.getId(), currentDate);

        // then
        assertThat(findAvailableSchedules.size()).isZero();
    }
}
