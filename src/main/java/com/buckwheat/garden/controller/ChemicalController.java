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
    public List<ChemicalDto.Response> getChemicalsByMemberId(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return chemicalService.getChemicalsByGardenerId(userPrincipal.getGardener().getGardenerId());
    }

    /**
     * 해당 약품의 사용 내역
     * @param chemicalId 약품 번호
     * @return 해당 약품의 주기 리스트(WateringDto)
     */
    @GetMapping("/{chemicalId}/watering")
    public List<WateringDto.ResponseInChemical> getWateringsByChemicalId(@PathVariable long chemicalId){
        return chemicalService.getWateringsByChemicalId(chemicalId);
    }

    /**
     * 비료 추가
     * @param userPrincipal SecurityContext에서 Gardener를 가져오기 위해
     * @param chemicalRequest 입력받은 약품 정보
     * @return 수정한 ChemicalDTO
     */
    @PostMapping("")
    public ChemicalDto.Response add(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChemicalDto.Request chemicalRequest){
        return chemicalService.add(userPrincipal.getGardener().getGardenerId(), chemicalRequest);
    }

    /**
     * 약품 수정
     * @param userPrincipal
     * @param chemicalRequest 입력받은 약품 정보
     * @return 수정한 Chemical
     */
    @PutMapping("/{chemicalId}")
    public ChemicalDto.Response modify(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChemicalDto.Request chemicalRequest){
        return chemicalService.modify(userPrincipal.getGardener().getGardenerId(), chemicalRequest);
    }

    /**
     * 약품 삭제
     * @param chemicalId
     */
    @DeleteMapping("/{chemicalId}")
    public void delete(@PathVariable long chemicalId){
        chemicalService.delete(chemicalId);
    }
}
