package com.buckwheat.garden.plant;

import com.buckwheat.garden.plant.plant.PlantInPlace;
import com.buckwheat.garden.plant.plant.PlantRequest;
import com.buckwheat.garden.gardener.token.UserPrincipal;
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
