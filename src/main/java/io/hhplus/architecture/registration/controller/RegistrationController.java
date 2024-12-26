package io.hhplus.architecture.registration.controller;

import io.hhplus.architecture.registration.controller.dto.request.RegistrationRequest;
import io.hhplus.architecture.registration.controller.dto.response.RegistrationResponse;
import io.hhplus.architecture.registration.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registrations/{id}")
    public ResponseEntity<Void> registerSchedule(
            @PathVariable Long id,
            @Valid @RequestBody RegistrationRequest request
    ) {
        long registrationId = registrationService.registerSchedule(id, request);
        return ResponseEntity.created(URI.create("/registrations/me/" + registrationId)).build();
    }

    @GetMapping("/registrations/{id}")
    public ResponseEntity<List<RegistrationResponse>> findRegistrations(
            @PathVariable Long id
    ) {
        List<RegistrationResponse> response = registrationService.findRegistrations(id);
        return ResponseEntity.ok(response);
    }
}
