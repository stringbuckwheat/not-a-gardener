package xyz.notagardener.watering.plant;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.plant.dto.PlantWateringResponse;
import xyz.notagardener.watering.plant.dto.WateringForOnePlant;

import java.util.List;

public interface PlantWateringService {
    PlantWateringResponse add(WateringRequest wateringRequest, Pageable pageable, Long gardenerId);

    List<WateringForOnePlant> getAll(Long plantId, Pageable pageable);

    PlantWateringResponse update(WateringRequest wateringRequest, Pageable pageable, Long gardenerId);

    void delete(Long wateringId, Long plantId, Long gardenerId);

    void deleteAll(Long plantId, Long gardenerId);
}
