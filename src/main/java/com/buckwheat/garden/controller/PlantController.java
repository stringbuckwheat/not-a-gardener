package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.ModifyPlantPlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.PlantRequestDto;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.service.PlantService;
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

    @GetMapping("/{plantNo}")
    public GardenDto getOnePlant(@PathVariable("plantNo") int plantNo){
        return plantService.getOnePlant(plantNo);
    }

    @GetMapping("")
    public List<PlantDto> getPlantList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return plantService.getPlantList(userPrincipal.getMember().getMemberNo());
    }

    @PostMapping("")
    public void addPlant(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PlantRequestDto plantRequestDto){
        plantRequestDto.setMember(userPrincipal.getMember());
        plantService.addPlant(plantRequestDto);
    }

    @PutMapping("/{plantNo}")
    public void modifyPlant(@PathVariable("plantNo") int plantNo, @RequestBody PlantRequestDto plantRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        log.debug("modify plant");
        plantRequestDto.setMemberNo(userPrincipal.getMember().getMemberNo());

        plantService.modifyPlant(plantRequestDto);
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
