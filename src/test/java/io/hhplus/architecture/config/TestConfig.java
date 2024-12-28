package io.hhplus.architecture.config;

import io.hhplus.architecture.schedule.repository.ScheduleRepository;
import io.hhplus.architecture.schedule.repository.fake.FakeScheduleCoreRepository;
import io.hhplus.architecture.schedule.repository.fake.FakeScheduleJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public ScheduleRepository scheduleRepository(FakeScheduleJpaRepository fakeScheduleJpaRepository) {
        return new FakeScheduleCoreRepository(fakeScheduleJpaRepository);
    }
}
