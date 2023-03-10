package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.*;
import com.buckwheat.garden.service.PlantService;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/plant")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;
    private final WateringService wateringService;

    @GetMapping("/{plantNo}")
    public PlantDto.PlantResponse getOnePlant(@PathVariable("plantNo") int plantNo){
        return plantService.getOnePlant(plantNo);
    }

    @GetMapping("")
    public List<PlantDto.PlantResponse> getPlantList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return plantService.getPlantList(userPrincipal.getMember().getMemberNo());
    }

    @GetMapping("/{plantNo}/watering")
    public List<WateringDto.WateringForOnePlant> getWateringListForPlant(@PathVariable("plantNo") int plantNo){
        return wateringService.getWateringListForPlant(plantNo);
    }

    @PostMapping("")
    public void addPlant(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PlantDto.PlantRequest plantRequestDto){
        plantService.addPlant(plantRequestDto, userPrincipal.getMember());
    }

    @PutMapping("/{plantNo}")
    public PlantDto.PlantResponse modifyPlant(@PathVariable("plantNo") int plantNo, @RequestBody PlantDto.PlantRequest plantRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        log.debug("modify plant dto: {}", plantRequestDto);
        return plantService.modifyPlant(plantRequestDto, userPrincipal.getMember());
    }

    @PutMapping("/modify-place")
    public void modifyPlantPlace(@RequestBody ModifyPlantPlaceDto modifyPlantPlaceDto){
        plantService.modifyPlantPlace(modifyPlantPlaceDto);
    }

    @DeleteMapping("/{plantNo}")
    public void deletePlant(@PathVariable("plantNo") int plantNo){
        log.debug("delete plant -> plantNo: " + plantNo);

        plantService.deletePlantByPlantNo(plantNo);
    }
}
