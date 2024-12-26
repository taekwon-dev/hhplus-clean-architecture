package io.hhplus.architecture.registration.repository;

import io.hhplus.architecture.registration.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {
}
