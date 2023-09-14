package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WateringService {
    Map<LocalDate, List<WateringDto.ByDate>> getAll(Long gardenerId, LocalDate date);
    WateringDto.ByDate add(WateringDto.Request wateringRequest);
    LocalDate getStartDate(LocalDate firstDayOfMonth);
    LocalDate getEndDate(LocalDate firstDayOfMonth);
    void delete(long wateringId);
}
