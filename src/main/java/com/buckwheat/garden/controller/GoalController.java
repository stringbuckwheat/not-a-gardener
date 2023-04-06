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
    private final GoalService goalService;

    @GetMapping("")
    public List<GoalDto.Response> getGoalList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.debug("getGoalList()");
        log.debug("userPrincipal.getMember().getMemberNo(): {}", userPrincipal.getMember().getMemberNo());

        return goalService.getGoalList(userPrincipal.getMember().getMemberNo());
    }

    @PostMapping("")
    public GoalDto.Response addGoal(@RequestBody GoalDto.Request goalDto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return goalService.addGoal(goalDto, userPrincipal.getMember());
    }

    @PutMapping("/{goalNo}")
    public GoalDto.Response modifyGoal(@RequestBody GoalDto.Request goalDto) {
        return goalService.modifyGoal(goalDto);
    }

    @PutMapping("/{goalNo}/complete")
    public GoalDto.Response completeGoal(@PathVariable int goalNo) {
        return goalService.completeGoal(goalNo);
    }

    @DeleteMapping("/{goalNo}")
    public void deleteGoal(@PathVariable int goalNo) {
        goalService.deleteGoal(goalNo);
    }
}
