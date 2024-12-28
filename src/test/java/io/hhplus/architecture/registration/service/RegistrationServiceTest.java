package io.hhplus.architecture.registration.service;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.registration.controller.dto.request.RegistrationRequest;
import io.hhplus.architecture.registration.controller.dto.response.RegistrationResponse;
import io.hhplus.architecture.registration.domain.Registration;
import io.hhplus.architecture.registration.exception.AlreadyRegisteredException;
import io.hhplus.architecture.registration.exception.InvalidScheduleException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        long registrationId = registrationService.registerSchedule(audience.getId(), new RegistrationRequest(schedule.getId()), LocalDateTime.now());
        
        // then
        Registration findRegistration = registrationRepository.findById(registrationId);
        Schedule findSchedule = scheduleRepository.findById(schedule.getId());

        assertThat(findRegistration.getAudience().getId()).isEqualTo(audience.getId());
        assertThat(findRegistration.getSchedule().getId()).isEqualTo(schedule.getId());
        assertThat(findSchedule.getCurrentAttendees()).isEqualTo(schedule.getCurrentAttendees() + 1);
    }

    @DisplayName("특강 신청 시점 기준 Grace Period(30분) 이내에 시작하는 특강 신청은 실패한다.")
    @Test
    void registerSchedule_FailureIsBeforeGracePeriod() {
        // given
        Member audience = memberRepository.save(MemberFixture.AUDIENCE());
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusMinutes(15);
        LocalDateTime endDate = startDate.plusHours(2);
        Schedule schedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));
        RegistrationRequest registrationRequest = new RegistrationRequest(schedule.getId());

        // when & then
        assertThatThrownBy(() -> registrationService.registerSchedule(audience.getId(), registrationRequest, LocalDateTime.now().plusMinutes(30)))
                .isInstanceOf(InvalidScheduleException.class);
    }

    /**
     * 40명이 동시에 특강 등록을 시도하고, 최대 정원 초과 시 예외 발생을 검증하는 테스트입니다. (성공: 30명, 실패: 10명)
     */
    @DisplayName("최대 정원 30명인 특강에서 30명 이후의 신청자는 특강 신청을 실패한다.")
    @Test
    void registerScheduleConcurrently_FailureAfterMaxCapacity() throws InterruptedException {
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
                registrationService.registerSchedule(audienceId, request, LocalDateTime.now());
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

    /**
     * 한 명이 동시에 5번 특강 등록을 시도, 최초 예약 성공을 제외하고 나머지 요청에 대해서는 예외가 발생함을 검증하는 테스트입니다. (성공: 1번, 실패: 4번)
     */
    @DisplayName("동일한 유저가 같은 특강을 동시에 5번 신청 했을 때 최초 한 번 성공을 제외하고는 특강 신청에 실패한다.")
    @Test
    void registerScheduleConcurrently_OnlyFirstSucceeds() throws InterruptedException {
        // given
        Member audience = memberRepository.save(MemberFixture.AUDIENCE());
        Member speaker = memberRepository.save(MemberFixture.SPEAKER());
        Seminar seminar = seminarRepository.save(SeminarFixture.create(speaker));

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.plusHours(1);
        LocalDateTime endDate = startDate.plusHours(2);
        Schedule schedule = scheduleRepository.save(ScheduleFixture.create(seminar, startDate, endDate));
        RegistrationRequest request = new RegistrationRequest(schedule.getId());

        int threads = 5;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        executeConcurrency(threads, () -> {
            try {
                registrationService.registerSchedule(audience.getId(), request, LocalDateTime.now());
                successCount.incrementAndGet();
            } catch (AlreadyRegisteredException e) {
                failCount.incrementAndGet();
            }
        });

        // then
        Schedule findSchedule = scheduleRepository.findById(schedule.getId());
        assertThat(findSchedule.getCurrentAttendees()).isEqualTo(successCount.get());
        assertThat(failCount.get()).isEqualTo(threads - successCount.get());
    }


    private void executeConcurrency(int threads, Runnable task) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(threads);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executor.execute(() -> {
                task.run();
                latch.countDown();
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
        registrationService.registerSchedule(audience.getId(), new RegistrationRequest(schedule.getId()), LocalDateTime.now());

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
