package com.buckwheat.garden.domain.watering.controller;

import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
import com.buckwheat.garden.domain.watering.service.PlantWateringService;
import com.buckwheat.garden.domain.watering.dto.PlantWateringResponse;
import com.buckwheat.garden.domain.watering.dto.WateringForOnePlant;
import com.buckwheat.garden.domain.watering.dto.WateringRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/watering")
public class PlantWateringController {
    private final PlantWateringService plantWateringService;

    @GetMapping("")
    public List<WateringForOnePlant> getAllWithPaging(@PathVariable long plantId, @PageableDefault(size = 10) Pageable pageable) {
        return plantWateringService.getAll(plantId, pageable);
    }

    @PostMapping("")
    public PlantWateringResponse add(@RequestBody WateringRequest wateringRequest, @PageableDefault(size = 10) Pageable pageable) {
        return plantWateringService.add(wateringRequest, pageable);
    }

    @PutMapping("/{wateringId}")
    public PlantWateringResponse modify(@RequestBody WateringRequest wateringRequest, @PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return plantWateringService.update(wateringRequest, pageable, userPrincipal.getId());
    }

    @DeleteMapping("/{wateringId}")
    public void delete(@PathVariable Long wateringId, @PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        plantWateringService.delete(wateringId, plantId, userPrincipal.getId());
    }

    @DeleteMapping("")
    public void deleteAll(@PathVariable long plantId) {
        plantWateringService.deleteAll(plantId);
    }
}
