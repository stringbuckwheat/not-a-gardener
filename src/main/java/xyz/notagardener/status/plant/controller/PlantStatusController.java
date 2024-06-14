package xyz.notagardener.status.plant.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.status.plant.dto.AddStatusResponse;
import xyz.notagardener.status.plant.dto.PlantStatusRequest;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;
import xyz.notagardener.status.plant.service.PlantStatusService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/status")
public class PlantStatusController {
    private final PlantStatusService plantStatusService;

    @PostMapping("")
    public ResponseEntity<AddStatusResponse> add(@RequestBody @Valid PlantStatusRequest request, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantStatusService.add(request, user.getId()));
    }

    @DeleteMapping("/{statusId}")
    public ResponseEntity<PlantStatusResponse> delete(@PathVariable Long plantId, @PathVariable Long statusId, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(plantStatusService.delete(plantId, statusId, user.getId()));
    }
}
