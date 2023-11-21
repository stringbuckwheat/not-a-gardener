package com.buckwheat.garden.watering;

import com.buckwheat.garden.watering.dto.WateringByDate;
import com.buckwheat.garden.watering.dto.WateringRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WateringService {
    Map<LocalDate, List<WateringByDate>> getAll(Long gardenerId, LocalDate date);

    WateringByDate add(WateringRequest wateringRequest);

    void delete(Long wateringId, Long plantId, Long gardenerId);
}
