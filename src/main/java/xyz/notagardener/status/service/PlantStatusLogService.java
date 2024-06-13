package xyz.notagardener.status.service;

import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.dto.StatusLogResponse;

import java.util.List;

public interface PlantStatusLogService {
    List<StatusLogResponse> getAllLog(Long plantId, Long gardenerId);

    @Transactional
    PlantStatusResponse deleteOne(Long statusLogId, Long gardenerId);
}
