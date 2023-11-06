package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.watering.WateringByDate;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/watering")
public class WateringController {
    private final WateringService wateringService;

    @GetMapping("/date/{date}")
    public Map<LocalDate, List<WateringByDate>> getAll(@AuthenticationPrincipal UserPrincipal user, @PathVariable String date){
        return wateringService.getAll(user.getId(), LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
    }

    @PostMapping("")
    public WateringByDate add(@RequestBody WateringRequest wateringRequest){
        return wateringService.add(wateringRequest);
    }

    @DeleteMapping("/{wateringId}")
    public void delete(@PathVariable Long wateringId, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody Map<String, Long> request){
        wateringService.delete(wateringId, request.get("plantId"), userPrincipal.getId());
    }
}
