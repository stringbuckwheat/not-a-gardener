package xyz.notagardener.watering.watering.service;

import xyz.notagardener.watering.watering.dto.WateringByDate;
import xyz.notagardener.watering.watering.dto.WateringRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WateringService {
    Map<LocalDate, List<WateringByDate>> getAll(Long gardenerId, LocalDate date);

    WateringByDate add(WateringRequest wateringRequest, Long gardenerId);

    void delete(Long wateringId, Long plantId, Long gardenerId);
}
