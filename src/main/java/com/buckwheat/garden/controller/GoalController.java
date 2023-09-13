package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/goal")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    @GetMapping("")
    public List<GoalDto.Basic> getAll(@AuthenticationPrincipal UserPrincipal user) {
        return goalService.getAll(user.getId());
    }

    @PostMapping("")
    public GoalDto.Basic add(@RequestBody GoalDto.Basic goalRequest, @AuthenticationPrincipal UserPrincipal user) {
        return goalService.add(user.getId(), goalRequest);
    }

    @PutMapping("/{goalId}")
    public GoalDto.Basic modify(@RequestBody GoalDto.Basic goalRequest) {
        return goalService.modify(goalRequest);
    }

    @PutMapping("/{goalId}/complete")
    public GoalDto.Basic complete(@PathVariable Long goalId) {
        return goalService.complete(goalId);
    }

    @DeleteMapping("/{goalId}")
    public void delete(@PathVariable Long goalId) {
        goalService.delete(goalId);
    }
}
