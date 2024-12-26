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

    @PostMapping("/registrations/{userId}")
    public ResponseEntity<Void> registerSchedule(
            @PathVariable Long userId,
            @Valid @RequestBody RegistrationRequest request
    ) {
        long registrationId = registrationService.registerSchedule(userId, request);
        return ResponseEntity.created(URI.create("/registrations/me/" + registrationId)).build();
    }

    @GetMapping("/registrations/{userId}")
    public ResponseEntity<List<RegistrationResponse>> findRegistrations(
            @PathVariable Long userId
    ) {
        List<RegistrationResponse> response = registrationService.findRegistrations(userId);
        return ResponseEntity.ok(response);
    }
}
