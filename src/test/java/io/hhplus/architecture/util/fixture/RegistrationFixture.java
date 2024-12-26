package io.hhplus.architecture.util.fixture;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.registration.domain.Registration;
import io.hhplus.architecture.schedule.domain.Schedule;

public class RegistrationFixture {

    public static Registration create(Member audience, Schedule schedule) {
        return new Registration(audience, schedule);
    }
}
