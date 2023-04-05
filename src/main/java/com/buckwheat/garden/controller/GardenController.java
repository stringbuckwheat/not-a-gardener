package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.service.GardenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/garden")
public class GardenController {
    private final GardenService gardenService;

    // garden 메인 페이지의 데이터 받아오기
    @GetMapping("")
    public GardenDto.GardenMain garden(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return gardenService.getGarden(userPrincipal.getMember().getMemberNo());
    }

    @GetMapping("/plants")
    public List<GardenDto.GardenResponse> getPlantList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return gardenService.getPlantList(userPrincipal.getMember().getMemberNo());
    }

    @PostMapping("/watering")
    public GardenDto.WateringResponse addWateringInGarden(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody WateringDto.WateringRequest wateringRequest){
        return gardenService.addWateringInGarden(userPrincipal.getMember(), wateringRequest);
    }
}
