package io.hhplus.architecture.registration.repository;

import io.hhplus.architecture.registration.domain.Registration;

public interface RegistrationRepository {

    Registration save(Registration registration);

    Registration findById(long id);
}
