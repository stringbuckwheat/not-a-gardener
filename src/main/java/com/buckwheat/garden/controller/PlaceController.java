package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("")
    public List<PlaceDto.PlaceCard> getPlaceList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return placeService.getPlaceList(userPrincipal.getMember().getMemberNo());
    }

    @GetMapping("/{placeNo}")
    public PlaceDto getPlace(@PathVariable int placeNo){
        return placeService.getPlace(placeNo);
    }

    @GetMapping("/{placeNo}/plant-list")
    public List<PlantDto.PlantInPlace> getPlantlistInPlace(@PathVariable int placeNo){
        log.debug("placeNo: {}", placeNo);
        return placeService.getPlantlistInPlace(placeNo);
    }

    @PostMapping("")
    public PlaceDto addPlace(@RequestBody PlaceDto placeDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return placeService.addPlace(placeDto, userPrincipal.getMember());
    }

    @PutMapping("/{placeNo}")
    public PlaceDto updatePlace(@RequestBody PlaceDto placeDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        placeService.modifyPlace(placeDto, userPrincipal.getMember());

        // 메소드 내부 호출 시 @Transactional이 작동하지 않아 일단 이렇게 처리
        return placeService.getPlace(placeDto.getPlaceNo());
    }

    @DeleteMapping("/{placeNo}")
    public void deletePlace(@PathVariable int placeNo){
        placeService.deletePlace(placeNo);
    }
}
