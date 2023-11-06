package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.garden.GardenResponse;
import com.buckwheat.garden.data.dto.place.ModifyPlace;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.data.dto.plant.PlantResponse;
import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.service.PlantService;
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
