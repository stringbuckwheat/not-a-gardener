package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.FertilizerDto;
import com.buckwheat.garden.service.FertilizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fertilizer")
@Slf4j
public class FertilizerController {
    private final FertilizerService fertilizerService;

    /**
     * 모든 비료 리스트 반환
     * @param userPrincipal SecurityContext에서 memberNo를 가져오기 위해
     * @return 모든 비료 리스트 반환
     */
    @GetMapping("")
    public List<FertilizerDto> fertilizerList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return fertilizerService.getFertilizerList(userPrincipal.getMember().getMemberNo());
    }

    /**
     * 비료 추가
     * @param userPrincipal SecurityContext에서 memberNo를 가져오기 위해
     * @param fertilizerDto 입력받은 비료 정보
     * @return 저장한 FertilizerDTO
     */
    @PostMapping("")
    public FertilizerDto addFertilizer(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody FertilizerDto fertilizerDto){
        fertilizerDto.setMemberNo(userPrincipal.getMember().getMemberNo());
        return fertilizerService.addFertilizer(fertilizerDto);
    }

    // 업데이트 메소드 필요 있어!

    @DeleteMapping("{fertilizerNo}")
    public void deleteFertilizer(@PathVariable int fertilizerNo){
        fertilizerService.deleteFertilizer(fertilizerNo);
    }

}
