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
     * @param user SecurityContext에서 memberId를 가져오기 위해
     * @return 모든 비료 리스트 반환
     */
    @GetMapping("")
    public List<ChemicalDto.Basic> getAll(@AuthenticationPrincipal UserPrincipal user){
        return chemicalService.getAll(user.getId());
    }

    /**
     * 해당 약품의 디테일 정보
     * @param chemicalId 약품 번호
     * @return 해당 약품의 주기 리스트(WateringDto)
     */
    @GetMapping("/{chemicalId}")
    public ChemicalDto.Detail getDetail(@PathVariable Long chemicalId, @AuthenticationPrincipal UserPrincipal user){
        return chemicalService.getDetail(chemicalId, user.getId());
    }

    /**
     * 비료 추가
     * @param user SecurityContext에서 Gardener를 가져오기 위해
     * @param chemicalRequest 입력받은 약품 정보
     * @return 수정한 ChemicalDTO
     */
    @PostMapping("")
    public ChemicalDto.Basic add(@AuthenticationPrincipal UserPrincipal user, @RequestBody ChemicalDto.Basic chemicalRequest){
        return chemicalService.add(user.getId(), chemicalRequest);
    }

    /**
     * 약품 수정
     * @param user
     * @param chemicalRequest 입력받은 약품 정보
     * @return 수정한 Chemical
     */
    @PutMapping("/{chemicalId}")
    public ChemicalDto.Basic modify(@AuthenticationPrincipal UserPrincipal user, @RequestBody ChemicalDto.Basic chemicalRequest){
        return chemicalService.modify(user.getId(), chemicalRequest);
    }

    /**
     * 약품 데이터 비활성화
     * 물주기 기록을 유지하기 위해 삭제X
     * @param chemicalId
     */
    @DeleteMapping("/{chemicalId}/deactivate")
    public void deactivate(@PathVariable Long chemicalId, @AuthenticationPrincipal UserPrincipal user){
        chemicalService.deactivate(chemicalId, user.getId());
    }
}
