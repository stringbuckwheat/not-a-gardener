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
@RequestMapping("/routine")
public class RoutineController {
    private final RoutineService routineService;

    @GetMapping("")
    public RoutineDto.Main getRoutineList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return routineService.getRoutineList(userPrincipal.getMember().getMemberNo());
    }

    @PostMapping("")
    public RoutineDto.Response addRoutine(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody RoutineDto.Request routineDto){
        log.debug("routineDto: {}", routineDto);
        return routineService.addRoutine(userPrincipal.getMember(), routineDto);
    }

    @PutMapping("/{routineNo}/complete")
    public RoutineDto.Response complete(@RequestBody RoutineDto.Complete routineDto){
        return routineService.complete(routineDto);
    }

    @PutMapping("/{routineNo}")
    public RoutineDto.Response modifyRoutine(@RequestBody RoutineDto.Request routineDto){
        return routineService.modifyRoutine(routineDto);
    }

    @DeleteMapping("/{routineNo}")
    public void deleteRoutine(@PathVariable int routineNo){
        routineService.deleteRoutine(routineNo);
    }
}
