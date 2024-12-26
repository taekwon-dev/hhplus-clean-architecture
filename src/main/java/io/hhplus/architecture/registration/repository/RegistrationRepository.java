package io.hhplus.architecture.registration.repository;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.registration.domain.Registration;
import io.hhplus.architecture.schedule.domain.Schedule;

import java.util.List;

public interface RegistrationRepository {

    Registration save(Registration registration);

    Registration findById(long id);

    List<Registration> findAllByAudienceId(long userId);

    boolean existsByAudienceAndSchedule(Member audience, Schedule schedule);
}
