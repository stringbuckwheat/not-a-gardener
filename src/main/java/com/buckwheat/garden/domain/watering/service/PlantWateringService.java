package com.buckwheat.garden.domain.watering.service;

import com.buckwheat.garden.domain.watering.dto.PlantWateringResponse;
import com.buckwheat.garden.domain.watering.dto.WateringForOnePlant;
import com.buckwheat.garden.domain.watering.dto.WateringRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlantWateringService {
    PlantWateringResponse add(WateringRequest wateringRequest, Pageable pageable);

    List<WateringForOnePlant> getAll(Long plantId, Pageable pageable);

    PlantWateringResponse update(WateringRequest wateringRequest, Pageable pageable, Long gardenerId);

    void delete(Long wateringId, Long plantId, Long gardenerId);

    void deleteAll(Long plantId);
}
