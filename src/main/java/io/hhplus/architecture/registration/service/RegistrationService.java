package io.hhplus.architecture.registration.service;

import io.hhplus.architecture.member.domain.Member;
import io.hhplus.architecture.member.repository.MemberRepository;
import io.hhplus.architecture.registration.controller.dto.RegistrationRequest;
import io.hhplus.architecture.registration.domain.Registration;
import io.hhplus.architecture.registration.repository.RegistrationRepository;
import io.hhplus.architecture.schedule.domain.Schedule;
import io.hhplus.architecture.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final RegistrationRepository registrationRepository;

    @Transactional
    public long registerSchedule(long userId, RegistrationRequest request) {
        Member audience = memberRepository.findById(userId);
        Schedule schedule = scheduleRepository.findById(request.scheduleId());
        schedule.incrementCurrentAttendees();

        Registration registration = registrationRepository.save(new Registration(audience, schedule));
        return registration.getId();
    }
}
