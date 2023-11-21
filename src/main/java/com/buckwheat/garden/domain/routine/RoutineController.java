package com.buckwheat.garden.domain.routine;

import com.buckwheat.garden.domain.gardener.token.UserPrincipal;
import com.buckwheat.garden.domain.routine.dto.RoutineComplete;
import com.buckwheat.garden.domain.routine.dto.RoutineMain;
import com.buckwheat.garden.domain.routine.dto.RoutineRequest;
import com.buckwheat.garden.domain.routine.dto.RoutineResponse;
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
    public RoutineMain getAll(@AuthenticationPrincipal UserPrincipal user) {
        return routineService.getAll(user.getId());
    }

    @PostMapping("")
    public RoutineResponse add(@AuthenticationPrincipal UserPrincipal user, @RequestBody RoutineRequest routineRequest) {
        return routineService.add(user.getId(), routineRequest);
    }

    @PutMapping("/{routineId}/complete")
    public RoutineResponse complete(@RequestBody RoutineComplete routineComplete) {
        return routineService.complete(routineComplete);
    }

    @PutMapping("/{routineId}")
    public RoutineResponse modify(@RequestBody RoutineRequest routineRequest) {
        return routineService.update(routineRequest);
    }

    @DeleteMapping("/{routineId}")
    public void deleteRoutine(@PathVariable Long routineId) {
        routineService.delete(routineId);
    }
}
