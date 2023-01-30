package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.PlantRequestDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.service.PlantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/garden/plant")
@RestController
@Slf4j
public class PlantController {
    @Autowired
    private PlantService plantService;

    @GetMapping("/{plantNo}")
    public PlantDto getOnePlant(@PathVariable("plantNo") int plantNo){
        return plantService.getOnePlant(plantNo);
    }

    @PostMapping("")
    public void addPlant(@AuthenticationPrincipal User user, @RequestBody PlantRequestDto plantRequestDto){
        plantRequestDto.setUsername(user.getUsername());
        plantService.addPlant(plantRequestDto);
    }

    @PutMapping("/{plantNo}")
    public void modifyPlant(@PathVariable("plantNo") int plantNo, @RequestBody PlantRequestDto plantRequestDto, @AuthenticationPrincipal User user){
        plantRequestDto.setUsername(user.getUsername());
        plantService.modifyPlant(plantRequestDto);
    }

    @DeleteMapping("/{plantNo}")
    public void deletePlant(@PathVariable("plantNo") int plantNo){
        log.debug("delete plant -> plantNo: " + plantNo);

        plantService.deletePlantByPlantNo(plantNo);
    }
}
