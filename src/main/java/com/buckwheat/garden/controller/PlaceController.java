package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.PlaceDto;
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

    /**
     * 유저의 전체 장소 리스트
     * @param userPrincipal
     * @return 유저의 전체 장소 리스트, 카드용 DTO에 담아 보낸다.
     */
    @GetMapping("")
    public List<PlaceDto.PlaceCard> getPlaceList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return placeService.getPlaceList(userPrincipal.getMember().getMemberNo());
    }

    /**
     * 하나의 장소 정보
     * @param placeNo
     * @return
     */
    @GetMapping("/{placeNo}")
    public PlaceDto.WithPlantList getPlace(@PathVariable int placeNo){
        return placeService.getPlace(placeNo);
    }

    /**
     * 장소 추가
     * @param placeRequestDto
     * @param userPrincipal
     * @return
     */
    @PostMapping("")
    public PlaceDto.PlaceResponseDto addPlace(@RequestBody PlaceDto.PlaceRequestDto placeRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return placeService.addPlace(placeRequestDto, userPrincipal.getMember());
    }

    /**
     * 장소 수정
     * @param placeRequestDto
     * @param userPrincipal Member
     * @return 수정한 장소 정보
     */
    @PutMapping("/{placeNo}")
    public PlaceDto.PlaceResponseDto modifyPlace(@RequestBody PlaceDto.PlaceRequestDto placeRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return placeService.modifyPlace(placeRequestDto, userPrincipal.getMember());
    }

    /**
     * 하나의 장소 삭제
     * @param placeNo
     */
    @DeleteMapping("/{placeNo}")
    public void deletePlace(@PathVariable int placeNo){
        placeService.deletePlace(placeNo);
    }
}
