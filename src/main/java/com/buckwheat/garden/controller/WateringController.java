package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/watering")
public class WateringController {
    private final WateringService wateringService;

    @GetMapping("/month/{month}")
    public Map<LocalDate, List<WateringDto.ByDate>> getWateringListByMonth(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int month){
        return wateringService.getWateringList(userPrincipal.getMember().getMemberNo(), month);
    }

    @PostMapping("")
    public WateringDto.WateringModifyResponse addWatering(@RequestBody WateringDto.WateringRequest wateringRequest){
        log.debug("wateringRequest: {}", wateringRequest);
        return wateringService.addWatering(wateringRequest);
    }

    @PutMapping("/{wateringNo}")
    public WateringDto.WateringModifyResponse updateWatering(@RequestBody WateringDto.WateringRequest wateringRequest){
        return wateringService.modifyWatering(wateringRequest);
    }

    @DeleteMapping("/{wateringNo}")
    public void deleteWatering(@PathVariable int wateringNo){
        wateringService.deleteWatering(wateringNo);
    }

    @DeleteMapping("/plant/{plantNo}")
    public void deleteAllFromPlant(@PathVariable int plantNo) {
        wateringService.deleteAllFromPlant(plantNo);
    }
}
