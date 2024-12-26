package io.hhplus.architecture.registration.repository;

import io.hhplus.architecture.registration.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {

    List<Registration> findAllByAudienceId(long userId);
}
