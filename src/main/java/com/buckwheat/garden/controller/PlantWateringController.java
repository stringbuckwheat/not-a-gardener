package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.watering.PlantWateringResponse;
import com.buckwheat.garden.data.dto.watering.WateringForOnePlant;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.service.PlantWateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
     *
     * @param plantId
     * @return
     */
    @GetMapping("")
    public List<WateringForOnePlant> getAllWithPaging(@PathVariable long plantId, @PageableDefault(size = 10) Pageable pageable) {
        return plantWateringService.getAll(plantId, pageable);
    }


    /**
     * 식물 상세 페이지에서 물주기
     *
     * @param wateringRequest
     * @return
     */
    @PostMapping("")
    public PlantWateringResponse add(@RequestBody WateringRequest wateringRequest, @PageableDefault(size = 10) Pageable pageable) {
        return plantWateringService.add(wateringRequest, pageable);
    }

    /**
     * 식물 상세페이지에서 물주기 수정
     *
     * @param wateringRequest
     * @return
     */
    @PutMapping("/{wateringId}")
    public PlantWateringResponse modify(@RequestBody WateringRequest wateringRequest, @PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return plantWateringService.update(wateringRequest, pageable, userPrincipal.getId());
    }

    /**
     * 한 식물의 물주기 기록 '한 개' 지우기
     *
     * @param wateringId
     */
    @DeleteMapping("/{wateringId}")
    public void delete(@PathVariable Long wateringId, @PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        plantWateringService.delete(wateringId, plantId, userPrincipal.getId());
    }

    /**
     * 해당 식물의 물주기 모두 지우기
     *
     * @param plantId
     */
    @DeleteMapping("")
    public void deleteAll(@PathVariable long plantId) {
        plantWateringService.deleteAll(plantId);
    }
}
