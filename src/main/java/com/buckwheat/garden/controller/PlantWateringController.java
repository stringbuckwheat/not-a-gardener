package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringForOnePlant;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.service.PlantWateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/watering")
public class PlantWateringController {
    private final PlantWateringService plantWateringService;

    /**
     * 한 식물의 물주기 기록 리스트
     * @param plantId
     * @return
     */
    @GetMapping("")
    public List<WateringForOnePlant> getAll(@PathVariable long plantId){
        return plantWateringService.getAll(plantId);
    }

    /**
     * 식물 상세 페이지에서 물주기
     * @param wateringRequest
     * @return
     */
    @PostMapping("")
    public AfterWatering add(@RequestBody WateringRequest wateringRequest){
        return plantWateringService.add(wateringRequest);
    }

    /**
     * 식물 상세페이지에서 물주기 수정
     * @param wateringRequest
     * @return
     */
    @PutMapping("/{wateringId}")
    public AfterWatering modify(@RequestBody WateringRequest wateringRequest){
        log.debug("request: {}", wateringRequest);
        return plantWateringService.modify(wateringRequest);
    }

    /**
     * 한 식물의 물주기 기록 '한 개' 지우기
     * @param wateringId
     */
    @DeleteMapping("/{wateringId}")
    public void delete(@PathVariable Long wateringId){
        plantWateringService.delete(wateringId);
    }

    /**
     * 해당 식물의 물주기 모두 지우기
     * @param plantId
     */
    @DeleteMapping("")
    public void deleteAll(@PathVariable long plantId) {
        plantWateringService.deleteAll(plantId);
    }
}
