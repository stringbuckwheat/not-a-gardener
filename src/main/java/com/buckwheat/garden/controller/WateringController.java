package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/watering")
public class WateringController {
    private final WateringService wateringService;

    @GetMapping("/month/{month}")
    public Map<LocalDate, List<WateringDto.ByDate>> getWateringListByMonth(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int month){
        return wateringService.getWateringList(userPrincipal.getMember().getMemberNo(), month);
    }

    @PostMapping("")
    public WateringDto.ByDate addWatering(@RequestBody WateringDto.Request wateringRequest){
        return wateringService.addWatering(wateringRequest);
    }
}
