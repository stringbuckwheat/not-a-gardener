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
@RequestMapping("/api/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    /**
     * 유저의 전체 장소 리스트
     * @param user
     * @return 유저의 전체 장소 리스트, 카드용 DTO에 담아 보낸다.
     */
    @GetMapping("")
    public List<PlaceDto.Card> getAll(@AuthenticationPrincipal UserPrincipal user){
        return placeService.getAll(user.getId());
    }

    /**
     * 하나의 장소 정보
     * @param placeId
     * @return
     */
    @GetMapping("/{placeId}")
    public PlaceDto.WithPlants getDetail(@PathVariable Long placeId, @AuthenticationPrincipal UserPrincipal user){
        return placeService.getDetail(placeId, user.getId());
    }

    /**
     * 장소 추가
     * @param placeRequest
     * @param user
     * @return
     */
    @PostMapping("")
    public PlaceDto.Card add(@RequestBody PlaceDto.Basic placeRequest, @AuthenticationPrincipal UserPrincipal user){
        return placeService.add(user.getId(), placeRequest);
    }

    /**
     * 장소 수정
     * @param placeRequest
     * @return 수정한 장소 정보
     */
    @PutMapping("/{placeId}")
    public PlaceDto.Basic modify(@RequestBody PlaceDto.Basic placeRequest, @AuthenticationPrincipal UserPrincipal user){
        return placeService.modify(placeRequest, user.getId());
    }

    /**
     * 하나의 장소 삭제
     * @param placeId
     */
    @DeleteMapping("/{placeId}")
    public void delete(@PathVariable long placeId, @AuthenticationPrincipal UserPrincipal user){
        placeService.delete(placeId, user.getId());
    }
}
