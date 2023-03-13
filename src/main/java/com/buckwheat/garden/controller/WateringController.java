package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/watering")
public class WateringController {
    private final WateringService wateringService;

    @GetMapping("")
    public List<WateringDto.WateringList> getWateringList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return wateringService.getWateringList(userPrincipal.getMember().getMemberNo());
    }

    @PostMapping("")
    public WateringDto.WateringResponse addWatering(@RequestBody  WateringDto.WateringRequest wateringRequest){
        log.debug("wateringRequest: {}", wateringRequest);
        return wateringService.addWatering(wateringRequest);
    }

    @DeleteMapping("/{wateringNo}")
    public void deleteWatering(@PathVariable int wateringNo){
        wateringService.deleteWatering(wateringNo);
    }
}
