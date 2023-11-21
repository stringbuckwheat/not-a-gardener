package com.buckwheat.garden.domain.plant.controller;

import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
import com.buckwheat.garden.domain.plant.service.GardenService;
import com.buckwheat.garden.domain.plant.dto.garden.GardenMain;
import com.buckwheat.garden.domain.plant.dto.garden.GardenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/garden")
public class GardenController {
    private final GardenService gardenService;

    @GetMapping("")
    public GardenMain getGardenMain(@AuthenticationPrincipal UserPrincipal user) {
        return gardenService.getGarden(user.getId());
    }

    @GetMapping("/plants")
    public List<GardenResponse> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return gardenService.getAll(user.getId());
    }
}
