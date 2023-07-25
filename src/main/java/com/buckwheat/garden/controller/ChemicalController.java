package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.service.ChemicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chemical")
@Slf4j
public class ChemicalController {
    private final ChemicalService chemicalService;

    /**
     * 모든 비료 리스트 반환
     * @param userPrincipal SecurityContext에서 memberId를 가져오기 위해
     * @return 모든 비료 리스트 반환
     */
    @GetMapping("")
    public List<ChemicalDto.Response> getAll(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return chemicalService.getAll(userPrincipal.getGardener().getGardenerId());
    }

    /**
     * 해당 약품의 디테일 정보
     * @param chemicalId 약품 번호
     * @return 해당 약품의 주기 리스트(WateringDto)
     */
    @GetMapping("/{chemicalId}")
    public ChemicalDto.Detail getDetail(@PathVariable long chemicalId, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return chemicalService.getDetail(chemicalId, userPrincipal.getGardener().getGardenerId());
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
     * 약품 데이터 비활성화
     * 물주기 기록을 유지하기 위해 삭제X
     * @param chemicalId
     */
    @DeleteMapping("/{chemicalId}/deactivate")
    public void deactivate(@PathVariable long chemicalId, @AuthenticationPrincipal UserPrincipal userPrincipal){
        chemicalService.deactivate(chemicalId, userPrincipal.getGardener().getGardenerId());
    }
}
