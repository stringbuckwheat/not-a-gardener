package com.buckwheat.garden.controller;

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
     * @param user SecurityContext에서 memberId를 가져온다 (user.getUsername())
     * @return 모든 비료 리스트 반환
     */
    @GetMapping("")
    public List<FertilizerDto> fertilizerList(@AuthenticationPrincipal User user){
        return fertilizerService.getFertilizerList(Integer.parseInt(user.getUsername()));
    }

    /**
     * 비료 추가
     * @param user SecurityContext에서 memberId를 가져온다 (user.getUsername())
     * @param fertilizerDto 입력받은 비료 정보
     * @return 저장한 FertilizerDTO
     */
    @PostMapping("")
    public FertilizerDto addFertilizer(@AuthenticationPrincipal User user, @RequestBody FertilizerDto fertilizerDto){
        fertilizerDto.setMemberNo(Integer.parseInt(user.getUsername()));
        return fertilizerService.addFertilizer(fertilizerDto);
    }

    // 업데이트 메소드 필요 없음

    @DeleteMapping("{fertilizerNo}")
    public void deleteFertilizer(@PathVariable int fertilizerNo){
        fertilizerService.deleteFertilizer(fertilizerNo);
    }

}
