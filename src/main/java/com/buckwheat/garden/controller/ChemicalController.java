package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.chemical.ChemicalDetail;
import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.dto.watering.WateringResponseInChemical;
import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.service.ChemicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public List<ChemicalDto> getAll(@AuthenticationPrincipal UserPrincipal user){
        return chemicalService.readAll(user.getId());
    }

    /**
     * 해당 약품의 디테일 정보
     * @param chemicalId 약품 번호
     * @return 해당 약품의 주기 리스트(WateringDto)
     */
    @GetMapping("/{chemicalId}")
    public ChemicalDetail getDetail(@PathVariable Long chemicalId, @AuthenticationPrincipal UserPrincipal user){
        return chemicalService.readOne(chemicalId, user.getId());
    }

    @GetMapping("/{chemicalId}/watering")
    public List<WateringResponseInChemical> getWateringWithPaging(@PathVariable Long chemicalId, @PageableDefault(size = 10) Pageable pageable){
        return chemicalService.readWateringsForChemical(chemicalId, pageable);
    }

    /**
     * 비료 추가
     * @param user SecurityContext에서 Gardener를 가져오기 위해
     * @param chemicalRequest 입력받은 약품 정보
     * @return 수정한 ChemicalDTO
     */
    @PostMapping("")
    public ChemicalDto postChemical(@AuthenticationPrincipal UserPrincipal user, @RequestBody ChemicalDto chemicalRequest){
        return chemicalService.create(user.getId(), chemicalRequest);
    }

    /**
     * 약품 수정
     * @param user
     * @param chemicalRequest 입력받은 약품 정보
     * @return 수정한 Chemical
     */
    @PutMapping("/{chemicalId}")
    public ChemicalDto update(@AuthenticationPrincipal UserPrincipal user, @RequestBody ChemicalDto chemicalRequest){
        return chemicalService.update(user.getId(), chemicalRequest);
    }

    /**
     * 약품 데이터 비활성화
     * 물주기 기록을 유지하기 위해 삭제X
     * @param chemicalId
     */
    @DeleteMapping("/{chemicalId}")
    public void delete(@PathVariable Long chemicalId, @AuthenticationPrincipal UserPrincipal user){
        chemicalService.deactivate(chemicalId, user.getId());
    }
}
