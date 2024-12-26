package io.hhplus.architecture.registration.service;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.registration.controller.dto.request.RegistrationRequest;
import io.hhplus.architecture.registration.controller.dto.response.RegistrationResponse;
import io.hhplus.architecture.registration.domain.Registration;
import io.hhplus.architecture.registration.exception.AlreadyRegisteredException;
import io.hhplus.architecture.registration.repository.RegistrationRepository;
import io.hhplus.architecture.registration.service.mapper.RegistrationMapper;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.schedule.exception.ExceedMaxAttendeesException;
import io.hhplus.architecture.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;

    @Transactional
    public long registerSchedule(long userId, RegistrationRequest request) {
        Member audience = memberRepository.findById(userId);
        Schedule schedule = scheduleRepository.findById(request.scheduleId());
        validateExceedMaxAttendees(schedule);
        validateAlreadyRegistered(audience, schedule);
        schedule.incrementCurrentAttendees();

        Registration registration = registrationRepository.save(new Registration(audience, schedule));
        return registration.getId();
    }

    private void validateExceedMaxAttendees(Schedule schedule) {
        if (schedule.getCurrentAttendees() >= schedule.getMaxAttendees()) {
            throw new ExceedMaxAttendeesException();
        }
    }

    private void validateAlreadyRegistered(Member audience, Schedule schedule) {
        if (registrationRepository.existsByAudienceAndSchedule(audience, schedule)) {
            throw new AlreadyRegisteredException();
        }
    }

    @Transactional(readOnly = true)
    public List<RegistrationResponse> findRegistrations(long userId) {
        Member audience = memberRepository.findById(userId);
        List<Registration> registrations = registrationRepository.findAllByAudienceId(audience.getId());
        return registrationMapper.mapToRegistrationResponse(registrations);
    }
}
