package io.hhplus.architecture.registration.repository;

import io.hhplus.architecture.registration.domain.Registration;
import io.hhplus.architecture.registration.exception.RegistrationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegistrationCoreRepository implements RegistrationRepository {

    private final RegistrationJpaRepository jpaRepository;

    @Override
    public Registration save(Registration registration) {
        return jpaRepository.save(registration);
    }

    @Override
    public Registration findById(long id) {
        return jpaRepository.findById(id).orElseThrow(RegistrationNotFoundException::new);
    }
}
