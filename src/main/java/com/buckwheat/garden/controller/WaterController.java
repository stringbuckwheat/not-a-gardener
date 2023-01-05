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
    public String water(@RequestBody WaterDto waterDto){
        log.debug("water controller");
        log.debug(waterDto.toString());

        return wateringService.addWatering(waterDto);
    }
}
