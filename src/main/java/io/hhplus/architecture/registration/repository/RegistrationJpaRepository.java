package io.hhplus.architecture.registration.repository;

import io.hhplus.architecture.registration.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {

    @Query("""
            SELECT r FROM Registration r
            JOIN FETCH r.schedule
            WHERE r.audience.id = :userId
            """)
    List<Registration> findAllByAudienceId(long userId);
}
