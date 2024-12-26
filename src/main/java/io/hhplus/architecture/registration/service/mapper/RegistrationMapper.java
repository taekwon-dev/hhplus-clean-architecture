package io.hhplus.architecture.registration.service.mapper;

import io.hhplus.architecture.registration.controller.dto.response.RegistrationResponse;
import io.hhplus.architecture.registration.domain.Registration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistrationMapper {

    public List<RegistrationResponse> mapToRegistrationResponse(List<Registration> registrations) {
        return registrations.stream()
                .map(registration -> new RegistrationResponse(
                        registration.getSchedule().getTitle(),
                        registration.getSchedule().getSpeakerName(),
                        registration.getSchedule().getDescription(),
                        registration.getSchedule().getStartDate(),
                        registration.getSchedule().getEndDate()
                )).toList();
    }
}
