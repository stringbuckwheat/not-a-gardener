package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/goal")
@RequiredArgsConstructor
public class GoalController {
    private GoalService goalService;

    @GetMapping("")
    public List<GoalDto> getGoalList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return goalService.getGoalList(userPrincipal.getMember().getMemberNo());
    }

    @PostMapping("")
    public GoalDto addGoal(@RequestBody GoalDto goalDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return goalService.addGoal(goalDto, userPrincipal.getMember());
    }

    @PutMapping("/{goalNo}")
    public GoalDto modifyGoal(@RequestBody GoalDto goalDto){
        return goalService.modifyGoal(goalDto);
    }

    @DeleteMapping("/{goalNo}")
    public void deleteGoal(@PathVariable int goalNo){
        goalService.deleteGoal(goalNo);
    }
}
