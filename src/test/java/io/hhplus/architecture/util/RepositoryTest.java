package io.hhplus.architecture.util;

import io.hhplus.architecture.member.repository.MemberCoreRepository;
import io.hhplus.architecture.registration.repository.RegistrationCoreRepository;
import io.hhplus.architecture.schedule.repository.ScheduleCoreRepository;
import io.hhplus.architecture.seminar.repository.SeminarCoreRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
        MemberCoreRepository.class,
        SeminarCoreRepository.class,
        ScheduleCoreRepository.class,
        RegistrationCoreRepository.class
})
public abstract class RepositoryTest {
}
