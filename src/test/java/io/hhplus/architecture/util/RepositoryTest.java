package io.hhplus.architecture.util;

import io.hhplus.architecture.member.repository.MemberCoreRepository;
import io.hhplus.architecture.seminar.repository.SeminarCoreRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({MemberCoreRepository.class, SeminarCoreRepository.class})
public class RepositoryTest {
}
