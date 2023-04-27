package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.service.PlacePlantService;
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
@RequestMapping("/place/{placeId}/plant")
public class PlacePlantController {
    private final PlacePlantService placePlantService;

    @PostMapping("")
    public PlantDto.PlantInPlace addPlantInPlace(@RequestBody PlantDto.Request plantRequest, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return placePlantService.addPlantInPlace(userPrincipal.getMember().getMemberId(), plantRequest);
    }
}
