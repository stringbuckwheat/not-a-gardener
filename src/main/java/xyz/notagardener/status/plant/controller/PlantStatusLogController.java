package xyz.notagardener.status.plant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;
import xyz.notagardener.status.plant.dto.StatusLogResponse;
import xyz.notagardener.status.plant.service.PlantStatusLogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/status/log")
@Slf4j
public class PlantStatusLogController {
    private final PlantStatusLogService plantStatusLogService;

    @GetMapping("")
    public ResponseEntity<List<StatusLogResponse>> getAllLog(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(plantStatusLogService.getAllLog(plantId, user.getId()));
    }

    @DeleteMapping("/{statusLogId}")
    public ResponseEntity<PlantStatusResponse> deleteOne(@PathVariable Long statusLogId, @AuthenticationPrincipal UserPrincipal user) {
        plantStatusLogService.deleteOne(statusLogId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
