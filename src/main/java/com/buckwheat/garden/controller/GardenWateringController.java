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
@RequestMapping("/garden/{plantNo}/watering")
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
        return gardenWateringService.addWateringInGarden(userPrincipal.getMember(), wateringRequest);
    }

    /**
     * 메인 페이지에서 화분이 아직 마르지 않았다고 요청
     * @param plantNo
     * @return
     */
    @PutMapping("/not-dry")
    public WateringDto.Message notDry(@PathVariable int plantNo){
        return gardenWateringService.notDry(plantNo);
    }

    /**
     * 물주기 (귀찮아서) 미루기
     * @param plantNo
     * @return 새로운 wateringCode;
     */
    @PutMapping("/postpone")
    public int postpone(@PathVariable int plantNo){
        return gardenWateringService.postpone(plantNo);
    }
}
