package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/watering")
public class WateringController {
    private final WateringService wateringService;

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
        log.debug("deleteAllFromPlant");
        wateringService.deleteAllFromPlant(plantNo);
    }
}
