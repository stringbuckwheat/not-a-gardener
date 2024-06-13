package xyz.notagardener.status.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.status.dto.PlantStatusRequest;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.service.PlantStatusService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/status")
public class PlantStatusController {
    private final PlantStatusService plantStatusService;

    @GetMapping("")
    public ResponseEntity<List<PlantStatusResponse>> getAll(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(plantStatusService.getAll(plantId, user.getId()));
    }

    @PostMapping("")
    public ResponseEntity<PlantStatusResponse> add(@RequestBody @Valid PlantStatusRequest request, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantStatusService.add(request, user.getId()));
    }

    @DeleteMapping("/{statusId}")
    public ResponseEntity<List<PlantStatusResponse>> delete(@PathVariable Long plantId, @PathVariable Long statusId, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(plantStatusService.delete(plantId, statusId, user.getId()));
    }
}