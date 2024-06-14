package xyz.notagardener.status.garden;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.plant.garden.dto.AttentionRequiredPlant;
import xyz.notagardener.status.plant.dto.PlantStatusRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/status")
public class GardenStatusController {
    private final GardenStatusService gardenStatusService;

    @PostMapping("")
    public ResponseEntity<AttentionRequiredPlant> add(@RequestBody @Valid PlantStatusRequest request, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gardenStatusService.add(request, user.getId()));
    }
}
