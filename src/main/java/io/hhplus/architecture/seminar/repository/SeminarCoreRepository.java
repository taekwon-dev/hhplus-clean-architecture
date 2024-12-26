package io.hhplus.architecture.seminar.repository;

import io.hhplus.architecture.seminar.domain.Seminar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeminarCoreRepository implements SeminarRepository {

    private final SeminarJpaRepository jpaRepository;

    @Override
    public Seminar save(Seminar seminar) {
        return jpaRepository.save(seminar);
    }
}
