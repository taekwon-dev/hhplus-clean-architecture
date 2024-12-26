package io.hhplus.architecture.registration.service;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.registration.controller.dto.request.RegistrationRequest;
import io.hhplus.architecture.registration.controller.dto.response.RegistrationResponse;
import io.hhplus.architecture.registration.domain.Registration;
import io.hhplus.architecture.registration.repository.RegistrationRepository;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.schedule.exception.ExceedMaxAttendeesException;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

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

    /**
     * 40명이 동시에 특강 등록을 시도하고, 최대 정원 초과 시 예외 발생을 검증하는 테스트입니다. (성공: 40명, 실패: 10명)
     */
    @DisplayName("최대 정원 30명인 특강에서 30명 이후의 신청자는 특강 신청을 실패한다.")
    @Test
    void registerScheduleInParallel() throws InterruptedException {
        // given
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusHours(1);
        LocalDateTime endDate = startDate.plusHours(2);
        Schedule schedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));
        RegistrationRequest request = new RegistrationRequest(schedule.getId());

        int userCount = 40;
        int threads = 40;
        long startUserId = 2;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 2; i <= 41; i++) {
            memberRepository.save(MemberFixture.AUDIENCE());
        }

        // when
        executeConcurrency(threads, userCount, startUserId, audienceId -> {
            try {
                registrationService.registerSchedule(audienceId, request);
                successCount.incrementAndGet();
            } catch (ExceedMaxAttendeesException e) {
                failCount.incrementAndGet();
            }
        });

        // then
        Schedule findSchedule = scheduleRepository.findById(schedule.getId());
        assertThat(findSchedule.getCurrentAttendees()).isEqualTo(successCount.get());
        assertThat(failCount.get()).isEqualTo(userCount - findSchedule.getMaxAttendees());
    }

    private void executeConcurrency(int threads, int userCount, long startUserId, Consumer<Long> task) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(threads);
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            long userId = (i % userCount) + startUserId;
            executor.execute(() -> {
                try {
                    task.accept(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
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
