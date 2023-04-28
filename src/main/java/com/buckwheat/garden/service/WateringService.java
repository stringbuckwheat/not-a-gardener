package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WateringService {
    Map<LocalDate, List<WateringDto.ByDate>> getWateringList(Long gardenerId, int month);
    WateringDto.ByDate addWatering(WateringDto.Request wateringRequest);
}
