package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.WaterDto;
import com.buckwheat.garden.service.WateringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WaterController {
    @Autowired
    private WateringService wateringService;

    @PostMapping("/garden/water")
    public int water(@RequestBody WaterDto waterDto){
        return wateringService.addWatering(waterDto);
    }
}
