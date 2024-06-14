package xyz.notagardener.status.plant.service;

import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;
import xyz.notagardener.status.plant.dto.StatusLogResponse;

import java.util.List;

public interface PlantStatusLogService {
    List<StatusLogResponse> getAllLog(Long plantId, Long gardenerId);

    @Transactional
    PlantStatusResponse deleteOne(Long statusLogId, Long gardenerId);
}
