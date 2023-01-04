package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.AddPlantDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.service.GardenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/garden")
public class GardenController {
    @Autowired
    private GardenService gardenService;

    // garden 메인 페이지의 데이터 받아오기
    @GetMapping("")
    public List<PlantDto> gardenMain(@AuthenticationPrincipal User user){
        // @AuthenticationPrincipal: UserDetailsService에서 리턴한 객체를 파라미터로 직접 받아 사용할 수 있도록 하는 어노테이션
        // -> 이 어노테이션을 사용하면 헤더에 첨부된 access token에서 로그인한 사용자의 정보를 받아와 사용할 수 있다.

        return gardenService.getPlantList(user.getUsername());
    }

    @PostMapping("/plant")
    public void addPlant(@AuthenticationPrincipal User user, @RequestBody AddPlantDto addPlantDto){
        addPlantDto.setUsername(user.getUsername());
        log.debug("addPlantDto: " + addPlantDto);

        gardenService.addPlant(addPlantDto);
    }

}
