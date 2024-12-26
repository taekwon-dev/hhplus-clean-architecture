package io.hhplus.architecture.registration.service;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.registration.controller.dto.request.RegistrationRequest;
import io.hhplus.architecture.registration.controller.dto.response.RegistrationResponse;
import io.hhplus.architecture.registration.domain.Registration;
import io.hhplus.architecture.registration.repository.RegistrationRepository;
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

class RegistrationServiceTest extends ServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SeminarRepository seminarRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationService registrationService;

    @DisplayName("특강을 신청한다.")
    @Test
    void registerSchedule() {
        // given
        Member audience = memberRepository.save(MemberFixture.AUDIENCE());
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusHours(1);
        LocalDateTime endDate = startDate.plusHours(2);
        Schedule schedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));

        // when
        long registrationId = registrationService.registerSchedule(audience.getId(), new RegistrationRequest(schedule.getId()));
        
        // then
        Registration findRegistration = registrationRepository.findById(registrationId);
        Schedule findSchedule = scheduleRepository.findById(schedule.getId());

        assertThat(findRegistration.getAudience().getId()).isEqualTo(audience.getId());
        assertThat(findRegistration.getSchedule().getId()).isEqualTo(schedule.getId());
        assertThat(findSchedule.getCurrentAttendees()).isEqualTo(schedule.getCurrentAttendees() + 1);
    }

    @DisplayName("신청 완료한 특강 목록을 조회한다.")
    @Test
    void findRegistrations() {
        // given
        Member audience = memberRepository.save(MemberFixture.AUDIENCE());
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusHours(1);
        LocalDateTime endDate = startDate.plusHours(2);
        Schedule schedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));
        registrationService.registerSchedule(audience.getId(), new RegistrationRequest(schedule.getId()));

        // when
        List<RegistrationResponse> findRegistrations = registrationService.findRegistrations(audience.getId());

        // then
        assertThat(findRegistrations.size()).isOne();
        assertThat(findRegistrations.get(0).scheduleId()).isEqualTo(schedule.getId());
        assertThat(findRegistrations.get(0).title()).isEqualTo(schedule.getTitle());
        assertThat(findRegistrations.get(0).speakerName()).isEqualTo(schedule.getSeminar().getSpeaker().getName());
        assertThat(findRegistrations.get(0).description()).isEqualTo(schedule.getSeminar().getDescription());
        assertThat(findRegistrations.get(0).startDate()).isEqualTo(schedule.getStartDate());
        assertThat(findRegistrations.get(0).endDate()).isEqualTo(schedule.getEndDate());
    }
}
