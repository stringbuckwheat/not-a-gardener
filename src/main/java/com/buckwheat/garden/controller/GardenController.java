package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.service.GardenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GardenController {
    private final GardenService gardenService;

    // garden 메인 페이지의 데이터 받아오기
    @GetMapping("/garden")
    public List<GardenDto> garden(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return gardenService.getGarden(userPrincipal.getMember().getMemberNo());
    }
}
