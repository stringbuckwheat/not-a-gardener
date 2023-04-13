package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.service.PlantWateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/plant/{plantNo}/watering")
public class PlantWateringController {
    private final PlantWateringService plantWateringService;

    /**
     * 한 식물의 물주기 기록 리스트
     * @param plantNo
     * @return
     */
    @GetMapping("")
    public List<WateringDto.WateringForOnePlant> getWateringListForPlant(@PathVariable(value = "plantNo") int plantNo){
        return plantWateringService.getWateringListForPlant(plantNo);
    }

    /**
     * 식물 상세 페이지에서 물주기
     * @param wateringRequest
     * @return
     */
    @PostMapping("")
    public WateringDto.AfterWatering addWatering(@RequestBody WateringDto.Request wateringRequest){
        return plantWateringService.addWatering(wateringRequest);
    }

    /**
     * 식물 상세페이지에서 물주기 수정
     * @param wateringRequest
     * @return
     */
    @PutMapping("/{wateringNo}")
    public WateringDto.AfterWatering updateWatering(@RequestBody WateringDto.Request wateringRequest){
        return plantWateringService.modifyWatering(wateringRequest);
    }

    /**
     * 한 식물의 물주기 기록 '한 개' 지우기
     * @param wateringNo
     */
    @DeleteMapping("/{wateringNo}")
    public void deleteWatering(@PathVariable("wateringNo") int wateringNo){
        plantWateringService.deleteWatering(wateringNo);
    }

    /**
     * 해당 식물의 물주기 모두 지우기
     * @param plantNo
     */
    @DeleteMapping("")
    public void deleteAllFromPlant(@PathVariable("plantNo") int plantNo) {
        plantWateringService.deleteAllFromPlant(plantNo);
    }
}
