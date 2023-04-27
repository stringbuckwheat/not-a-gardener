package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.service.GardenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/garden")
public class GardenController {
    private final GardenService gardenService;

    /**
     * 로그인 후 메인페이지(오늘 할일 및 데이터를 기다리는 식물 정보)에 쓸 데이터
     * @param userPrincipal SecurityContext에 저장해놓은 memberNo
     * @return GardenDto.GardenMain
     *      List<Response> todoList: 오늘 할 일이 있는 식물 리스트
     *      List<GardenDto.WaitingForWatering> waitingList: 물주기 데이터를 기다리는 식물 리스트
     *      List<RoutineDto.Response> routineList: 루틴으로 등록된 할일 리스트
     */
    @GetMapping("")
    public GardenDto.GardenMain garden(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return gardenService.getGarden(userPrincipal.getMember().getMemberId());
    }

    /**
     * 식물 메인페이지 식물 리스트
     * @param userPrincipal SecurityContext에 저장해놓은 memberNo
     * @return 모든 식물의 GardenDto.Response 리스트
     *     PlantDto.Response 식물 테이블 정보의 DTO
     *     Detail 최근 관수일, 남은 관수 주기 계산, 관수 코드, 비료 코드 등을 계산한 정보
     */
    @GetMapping("/plants")
    public List<GardenDto.Response> getPlantsByMemberId(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return gardenService.getPlantsByMemberId(userPrincipal.getMember().getMemberId());
    }
}
