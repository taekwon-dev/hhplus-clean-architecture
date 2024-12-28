package io.hhplus.architecture.registration.service;

import io.hhplus.architecture.config.TestConfig;
import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.registration.controller.dto.request.RegistrationRequest;
import io.hhplus.architecture.registration.exception.AlreadyRegisteredException;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.schedule.exception.ExceedMaxAttendeesException;
import io.hhplus.architecture.schedule.repository.ScheduleRepository;
import io.hhplus.architecture.seminar.domain.Seminar;
import io.hhplus.architecture.seminar.repository.SeminarRepository;
import io.hhplus.architecture.util.fixture.MemberFixture;
import io.hhplus.architecture.util.fixture.ScheduleFixture;
import io.hhplus.architecture.util.fixture.SeminarFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestConfig.class)
@ActiveProfiles("test")
public class FakeRegistrationServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SeminarRepository seminarRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RegistrationService registrationService;

    /**
     * 동시성 제어를 적용하지 않은 Fake Repository 구현체를 주입 받았을 때, 최대 정원이 초과되어도 특강 신청이 허용되는 상황을 검증하는 테스트입니다.
     */
    @DisplayName("30명 정원의 특강에 40명의 서로 다른 유저가 신청할 때, 정원 초과 시에도 모든 유저가 신청에 성공한다.")
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
        int threads = 20;
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
        assertThat(successCount.get()).isEqualTo(40);
        assertThat(failCount.get()).isZero();
    }

    private void executeConcurrency(int threads, int userCount, long startUserId, Consumer<Long> task) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(userCount);
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < userCount; i++) {
            long userId = i + startUserId;
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
     * 동시성 제어를 적용하지 않은 Fake Repository 구현체를 주입 받았을 때, 한 특강에 대해 동일한 유저가 두 번 이상 신청에 성공하는 상황을 검증하는 테스트입니다.
     */
    @DisplayName("동일한 유저가 같은 특강을 동시에 5번 신청 했을 때 중복 신청이 된다.")
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
        assertThat(successCount.get()).isBetween(2, 5);
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
}
