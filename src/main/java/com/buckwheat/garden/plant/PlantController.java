package com.buckwheat.garden.plant;

import com.buckwheat.garden.plant.garden.GardenResponse;
import com.buckwheat.garden.place.dto.ModifyPlace;
import com.buckwheat.garden.place.dto.PlaceDto;
import com.buckwheat.garden.plant.plant.PlantRequest;
import com.buckwheat.garden.plant.plant.PlantResponse;
import com.buckwheat.garden.gardener.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/plant")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;

    @GetMapping("")
    public List<PlantResponse> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return plantService.getAll(user.getId());
    }

    @GetMapping("/{plantId}")
    public PlantResponse getDetail(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user) {
        return plantService.getDetail(plantId, user.getId());
    }

    @PostMapping("")
    public GardenResponse add(@AuthenticationPrincipal UserPrincipal user, @RequestBody PlantRequest plantRequest) {
        return plantService.add(user.getId(), plantRequest);
    }

    @PutMapping("/{plantId}")
    public GardenResponse modify(@RequestBody PlantRequest plantRequest, @AuthenticationPrincipal UserPrincipal user) {
        return plantService.update(user.getId(), plantRequest);
    }

    @PutMapping("/place/{placeId}")
    public PlaceDto modifyPlace(@RequestBody ModifyPlace modifyPlace, @AuthenticationPrincipal UserPrincipal user) {
        return plantService.updatePlace(modifyPlace, user.getId());
    }

    @DeleteMapping("/{plantId}")
    public void delete(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user) {
        plantService.delete(plantId, user.getId());
    }
}
