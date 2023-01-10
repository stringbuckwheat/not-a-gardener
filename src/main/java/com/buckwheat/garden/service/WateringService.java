package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WaterDto;

public interface WateringService {
    int addWatering(WaterDto waterDto);
    int getWateringPeriodCode(int plantNo);
}
