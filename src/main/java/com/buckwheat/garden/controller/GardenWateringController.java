package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.garden.GardenWateringResponse;
import com.buckwheat.garden.data.dto.watering.WateringMessage;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.service.GardenWateringService;
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
