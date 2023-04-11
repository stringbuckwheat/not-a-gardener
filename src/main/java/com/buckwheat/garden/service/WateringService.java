package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WateringService {
    Map<LocalDate, List<WateringDto.ByDate>> getWateringList(int memberNo, int month);
    WateringDto.ByDate addWatering(WateringDto.WateringRequest wateringRequest);
}
