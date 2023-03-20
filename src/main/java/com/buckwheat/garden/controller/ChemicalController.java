package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.service.ChemicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chemical")
@Slf4j
public class ChemicalController {
    private final ChemicalService chemicalService;

    /**
     * 모든 비료 리스트 반환
     * @param userPrincipal SecurityContext에서 memberNo를 가져오기 위해
     * @return 모든 비료 리스트 반환
     */
    @GetMapping("")
    public List<ChemicalDto.ChemicalResponse> getChemicalList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        log.debug("chemicalList: {}", chemicalService.getChemicalList(userPrincipal.getMember().getMemberNo()));
        return chemicalService.getChemicalList(userPrincipal.getMember().getMemberNo());
    }

    /**
     * 해당 약품의 사용 내역
     * @param chemicalNo 약품 번호
     * @return 해당 약품의 주기 리스트(WateringDto)
     */
    @GetMapping("/{chemicalNo}/watering-list")
    public List<WateringDto.WateringResponseInChemical> getWateringListByChemical(@PathVariable int chemicalNo){
        return chemicalService.getWateringListByChemical(chemicalNo);
    }

    /**
     * 비료 추가
     * @param userPrincipal SecurityContext에서 memberNo를 가져오기 위해
     * @param chemicalRequest 입력받은 비료 정보
     * @return 저장한 ChemicalDTO
     */
    @PostMapping("")
    public ChemicalDto.ChemicalResponse addChemical(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChemicalDto.ChemicalRequest chemicalRequest){
        return chemicalService.addChemical(chemicalRequest, userPrincipal.getMember());
    }

    @PutMapping("/{chemicalNo}")
    public ChemicalDto.ChemicalResponse updateChemical(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChemicalDto.ChemicalRequest chemicalRequest){
        log.debug("chemicalRequest: {}", chemicalRequest);
        return chemicalService.updateChemical(chemicalRequest, userPrincipal.getMember());
    }

    @DeleteMapping("/{chemicalNo}")
    public void deleteChemical(@PathVariable int chemicalNo){
        chemicalService.deleteChemical(chemicalNo);
    }
}
