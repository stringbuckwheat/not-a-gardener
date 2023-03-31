package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.*;
import com.buckwheat.garden.service.PlantService;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/plant")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;
    private final WateringService wateringService;

    /**
     * 전체 식물 리스트
     * 실제 식물 페이지가 아닌 다른 곳(ex. 장소페이지)에서 간단한 정보를 띄울 때 사용
     * @param userPrincipal
     * @return
     */
    @GetMapping("")
    public List<PlantDto.PlantResponse> getPlantList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return plantService.getPlantList(userPrincipal.getMember().getMemberNo());
    }

    /**
     * 한 식물의 정보
     * @param plantNo
     * @return
     */
    @GetMapping("/{plantNo}")
    public PlantDto.PlantResponse getOnePlant(@PathVariable("plantNo") int plantNo){
        return plantService.getOnePlant(plantNo);
    }

    /**
     * 한 식물의 물주기 기록 리스트
     * @param plantNo
     * @return
     */
    @GetMapping("/{plantNo}/watering")
    public List<WateringDto.WateringForOnePlant> getWateringListForPlant(@PathVariable("plantNo") int plantNo){
        return wateringService.getWateringListForPlant(plantNo);
    }

    /**
     * 식물 추가
     * @param userPrincipal
     * @param plantRequestDto
     * @return
     */
    @PostMapping("")
    public PlantDto.PlantInPlace addPlant(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PlantDto.PlantRequest plantRequestDto){
        return plantService.addPlant(plantRequestDto, userPrincipal.getMember());
    }

    /**
     * 식물 수정
     * @param plantRequestDto
     * @param userPrincipal
     * @return
     */
    @PutMapping("/{plantNo}")
    public GardenDto.GardenResponse modifyPlant(@RequestBody PlantDto.PlantRequest plantRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return plantService.modifyPlant(plantRequestDto, userPrincipal.getMember());
    }

    /**
     * 물주기 미루기(화분 덜 말랐을 때)
     * @param plantNo
     * @return
     */
    @PutMapping("/{plantNo}/averageWateringPeriod")
    public PlantDto.PlantResponse postponeAverageWateringPeriod(@PathVariable("plantNo") int plantNo){
        return plantService.postponeAverageWateringPeriod(plantNo);
    }

    /**
     * 여러 식물의 장소를 한 번에 바꿈
     * @param modifyPlantPlaceDto
     * @return
     */
    @PutMapping("/modify-place")
    public PlaceDto.PlaceResponseDto modifyPlantPlace(@RequestBody PlaceDto.ModifyPlantPlaceDto modifyPlantPlaceDto){
        return plantService.modifyPlantPlace(modifyPlantPlaceDto);
    }

    /**
     * 식물 삭제
     * @param plantNo
     */
    @DeleteMapping("/{plantNo}")
    public void deletePlant(@PathVariable("plantNo") int plantNo){
        plantService.deletePlantByPlantNo(plantNo);
    }
}
