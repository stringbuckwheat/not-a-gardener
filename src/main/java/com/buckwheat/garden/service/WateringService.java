package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.watering.WateringByDate;
import com.buckwheat.garden.data.dto.watering.WateringRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WateringService {
    Map<LocalDate, List<WateringByDate>> getAll(Long gardenerId, LocalDate date);
    WateringByDate add(WateringRequest wateringRequest);
    LocalDate getStartDate(LocalDate firstDayOfMonth);
    LocalDate getEndDate(LocalDate firstDayOfMonth);
    void delete(long wateringId);
}
