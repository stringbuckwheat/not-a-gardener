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

@RequestMapping("/api/plant")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;

    /**
     * 전체 식물 리스트
     * 실제 식물 페이지가 아닌 다른 곳(ex. 장소 페이지)에서 간단한 정보를 띄울 때 사용
     * @param user
     * @return
     */
    @GetMapping("")
    public List<PlantDto.Response> getAll(@AuthenticationPrincipal UserPrincipal user){
        return plantService.getAll(user.getId());
    }

    /**
     * 한 식물의 정보
     * @param plantId
     * @return
     */
    @GetMapping("/{plantId}")
    public PlantDto.Detail getDetail(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user){
        return plantService.getDetail(plantId, user.getId());
    }

    /**
     * 식물 추가
     * @param user
     * @param plantRequest
     * @return
     */
    @PostMapping("")
    public GardenDto.Response add(@AuthenticationPrincipal UserPrincipal user, @RequestBody PlantDto.Request plantRequest){
        return plantService.add(user.getId(), plantRequest);
    }

    /**
     * 식물 수정
     * @param plantRequest
     * @param user
     * @return
     */
    @PutMapping("/{plantId}")
    public GardenDto.Response modify(@RequestBody PlantDto.Request plantRequest, @AuthenticationPrincipal UserPrincipal user){
        return plantService.modify(user.getId(), plantRequest);
    }

    /**
     * 여러 식물의 장소를 한 번에 바꿈
     * @param modifyPlace
     * @return
     */
    @PutMapping("/modify-place")
    public PlaceDto.Response modifyPlace(@RequestBody PlaceDto.ModifyPlace modifyPlace, @AuthenticationPrincipal UserPrincipal user){
        return plantService.modifyPlace(modifyPlace, user.getId());
    }

    /**
     * 식물 삭제
     * @param plantId
     */
    @DeleteMapping("/{plantId}")
    public void delete(@PathVariable Long plantId, @AuthenticationPrincipal UserPrincipal user){
        plantService.delete(plantId, user.getId());
    }
}
