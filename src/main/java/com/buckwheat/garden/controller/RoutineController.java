package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.service.RoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine")
public class RoutineController {
    private final RoutineService routineService;

    @GetMapping("")
    public RoutineDto.Main getAll(@AuthenticationPrincipal UserPrincipal user){
        return routineService.getAll(user.getId());
    }

    @PostMapping("")
    public RoutineDto.Response add(@AuthenticationPrincipal UserPrincipal user, @RequestBody RoutineDto.Request routineRequest){
        return routineService.add(user.getId(), routineRequest);
    }

    @PutMapping("/{routineId}/complete")
    public RoutineDto.Response complete(@RequestBody RoutineDto.Complete routineComplete){
        return routineService.complete(routineComplete);
    }

    @PutMapping("/{routineId}")
    public RoutineDto.Response modify(@RequestBody RoutineDto.Request routineRequest){
        return routineService.modify(routineRequest);
    }

    @DeleteMapping("/{routineId}")
    public void deleteRoutine(@PathVariable Long routineId){
        routineService.delete(routineId);
    }
}
