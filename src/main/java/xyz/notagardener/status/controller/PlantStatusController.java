package xyz.notagardener.status.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.status.dto.PlantStatusRequest;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.service.PlantStatusService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PlantStatusController {
    private final PlantStatusService plantStatusService;

    @PostMapping("/api/plant/{plantId}/status")
    public ResponseEntity<PlantStatusResponse> add(@RequestBody @Valid PlantStatusRequest request, @AuthenticationPrincipal UserPrincipal user) {
        log.debug("Request: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(plantStatusService.add(request, user.getId()));
    }
}
