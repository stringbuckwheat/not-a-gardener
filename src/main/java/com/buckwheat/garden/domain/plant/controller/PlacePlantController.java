package com.buckwheat.garden.domain.plant.controller;

import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
import com.buckwheat.garden.domain.plant.service.PlacePlantService;
import com.buckwheat.garden.domain.plant.dto.plant.PlantInPlace;
import com.buckwheat.garden.domain.plant.dto.plant.PlantRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/place/{placeId}/plant")
public class PlacePlantController {
    private final PlacePlantService placePlantService;

    @PostMapping("")
    public PlantInPlace addPlantInPlace(@RequestBody PlantRequest plantRequest, @AuthenticationPrincipal UserPrincipal user) {
        return placePlantService.addPlantInPlace(user.getId(), plantRequest);
    }
}
