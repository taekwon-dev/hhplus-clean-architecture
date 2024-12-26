package io.hhplus.architecture.registration.repository;

import io.hhplus.architecture.registration.domain.Registration;

import java.util.List;

public interface RegistrationRepository {

    Registration save(Registration registration);

    Registration findById(long id);

    List<Registration> findAllByAudienceId(long userId);
}
