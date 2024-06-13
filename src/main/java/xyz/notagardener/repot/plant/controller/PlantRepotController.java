package xyz.notagardener.repot.plant.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.repot.plant.dto.RepotList;
import xyz.notagardener.repot.plant.service.PlantRepotService;
import xyz.notagardener.repot.repot.dto.RepotRequest;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/plant/{plantId}/repot")
public class PlantRepotController {
    private final PlantRepotService plantRepotService;

    @GetMapping("")
    public ResponseEntity<List<RepotList>> get(@PathVariable Long plantId, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(plantRepotService.getAllRepotForOnePlant(plantId, pageable));
    }

    @PostMapping("")
    public ResponseEntity<RepotList> add(@RequestBody @Valid RepotRequest repotRequest, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantRepotService.add(repotRequest, user.getId()));
    }
}
