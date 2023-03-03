package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.PesticideDto;
import com.buckwheat.garden.service.PesticideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/pesticide")
public class PesticideController {
    private final PesticideService pesticideService;

    @GetMapping("")
    List<PesticideDto> getPesticideList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return pesticideService.getPesticideList(userPrincipal.getMember().getMemberNo());
    }

    @GetMapping("/{pesticideNo}")
    PesticideDto getOnePesticide(@PathVariable int pesticideNo){
        return pesticideService.getOnePesticide(pesticideNo);
    }

    @PostMapping("")
    PesticideDto addPesticide(@RequestBody PesticideDto pesticideDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return pesticideService.addPesticide(pesticideDto, userPrincipal.getMember());
    }

    @PutMapping("/{pesticideNo}")
    PesticideDto modifyPesticide(@RequestBody PesticideDto pesticideDto){
        return pesticideService.modifyPesticide(pesticideDto);
    }

    @DeleteMapping("/{pesticideNo}")
    void deletePesticide(@PathVariable int pesticideNo){
        pesticideService.deletePesticide(pesticideNo);
    }
}
