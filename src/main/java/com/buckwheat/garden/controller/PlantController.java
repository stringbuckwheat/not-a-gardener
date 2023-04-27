package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.service.PlantService;
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

    /**
     * 전체 식물 리스트
     * 실제 식물 페이지가 아닌 다른 곳(ex. 장소 페이지)에서 간단한 정보를 띄울 때 사용
     * @param userPrincipal
     * @return
     */
    @GetMapping("")
    public List<PlantDto.Response> getPlantsByMemberId(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return plantService.getPlantsByMemberId(userPrincipal.getMember().getMemberId());
    }

    /**
     * 한 식물의 정보
     * @param plantId
     * @return
     */
    @GetMapping("/{plantId}")
    public PlantDto.Response getOnePlant(@PathVariable long plantId){
        return plantService.getPlantDetail(plantId);
    }

    /**
     * 식물 추가
     * @param userPrincipal
     * @param plantRequest
     * @return
     */
    @PostMapping("")
    public GardenDto.Response add(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PlantDto.Request plantRequest){
        return plantService.add(userPrincipal.getMember().getMemberId(), plantRequest);
    }

    /**
     * 식물 수정
     * @param plantRequest
     * @param userPrincipal
     * @return
     */
    @PutMapping("/{plantId}")
    public GardenDto.Response modify(@RequestBody PlantDto.Request plantRequest, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return plantService.modify(userPrincipal.getMember().getMemberId(), plantRequest);
    }

    /**
     * 여러 식물의 장소를 한 번에 바꿈
     * @param modifyPlantPlace
     * @return
     */
    @PutMapping("/modify-place")
    public PlaceDto.Response modifyPlantPlace(@RequestBody PlaceDto.ModifyPlantPlace modifyPlantPlace){
        return plantService.modifyPlantPlace(modifyPlantPlace);
    }

    /**
     * 식물 삭제
     * @param plantId
     */
    @DeleteMapping("/{plantId}")
    public void deletePlant(@PathVariable long plantId){
        plantService.delete(plantId);
    }
}
