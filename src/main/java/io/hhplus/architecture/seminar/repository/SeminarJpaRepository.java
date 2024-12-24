package io.hhplus.architecture.seminar.repository;

import io.hhplus.architecture.seminar.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeminarJpaRepository extends JpaRepository<Seminar, Long> {
}
