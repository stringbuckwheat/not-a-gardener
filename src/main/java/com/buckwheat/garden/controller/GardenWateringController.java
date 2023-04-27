package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.service.GardenWateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/garden/{plantId}/watering")
@Slf4j
public class GardenWateringController {
    private final GardenWateringService gardenWateringService;

    /**
     * 메인 페이지에서 물주기 추가하기
     * @param userPrincipal
     * @param wateringRequest
     * @return
     */
    @PostMapping("")
    public GardenDto.WateringResponse addWateringInGarden(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody WateringDto.Request wateringRequest){
        return gardenWateringService.addWateringInGarden(userPrincipal.getMember().getMemberId(), wateringRequest);
    }

    /**
     * 메인 페이지에서 화분이 아직 마르지 않았다고 요청
     * @param plantId
     * @return
     */
    @PutMapping("/not-dry")
    public WateringDto.Message notDry(@PathVariable long plantId){
        return gardenWateringService.notDry(plantId);
    }

    /**
     * 물주기 (귀찮아서) 미루기
     * @param plantId
     * @return 새로운 wateringCode;
     */
    @PutMapping("/postpone")
    public int postpone(@PathVariable long plantId){
        return gardenWateringService.postpone(plantId);
    }
}
