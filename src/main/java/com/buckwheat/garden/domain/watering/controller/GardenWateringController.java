package com.buckwheat.garden.domain.watering.controller;

import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
import com.buckwheat.garden.domain.plant.dto.garden.GardenWateringResponse;
import com.buckwheat.garden.domain.watering.service.GardenWateringService;
import com.buckwheat.garden.domain.watering.dto.WateringMessage;
import com.buckwheat.garden.domain.watering.dto.WateringRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/garden/{plantId}/watering")
@Slf4j
public class GardenWateringController {
    private final GardenWateringService gardenWateringService;

    @PostMapping("")
    public GardenWateringResponse add(@AuthenticationPrincipal UserPrincipal user, @RequestBody WateringRequest wateringRequest) {
        return gardenWateringService.add(user.getId(), wateringRequest);
    }

    @PutMapping("/not-dry")
    public WateringMessage notDry(@PathVariable Long plantId) {
        return gardenWateringService.notDry(plantId);
    }

    @PutMapping("/postpone")
    public int postpone(@PathVariable Long plantId) {
        return gardenWateringService.postpone(plantId);
    }
}
