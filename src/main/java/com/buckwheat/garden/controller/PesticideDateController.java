package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.PesticideDateDto;
import com.buckwheat.garden.data.entity.PesticideDate;
import com.buckwheat.garden.service.PesticideDateService;
import com.buckwheat.garden.service.PesticideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pesticide-date")
public class PesticideDateController {
    private final PesticideDateService pesticideDateService;

    @GetMapping("")
    public List<PesticideDateDto> getPesticideDateList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return pesticideDateService.getPesticideDateListByPesticide(userPrincipal.getMember().getMemberNo());
    }

    @GetMapping("/{pesticideDateNo}")
    public PesticideDateDto getPesticideDate(@PathVariable int pesticideDateNo){
        return pesticideDateService.getPesticideDate(pesticideDateNo);
    }

    @PostMapping("")
    public PesticideDateDto addPesticideDate(@RequestBody PesticideDateDto pesticideDateDto){
        return pesticideDateService.addPesticideDate(pesticideDateDto);
    }

    @PutMapping("/{pesticideDateNo}")
    public PesticideDateDto modifyPesticideDate(@RequestBody PesticideDateDto pesticideDateDto){
        return pesticideDateService.modifyPesticideDate(pesticideDateDto);
    }

    @DeleteMapping("/{pesticideDateNo}")
    public void deletePesticideDate(@PathVariable int pesticideDateNo){
        pesticideDateService.deletePesticideDate(pesticideDateNo);
    }
}
