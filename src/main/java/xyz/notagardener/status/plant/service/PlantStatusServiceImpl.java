package xyz.notagardener.status.plant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.status.common.model.Status;
import xyz.notagardener.status.common.model.StatusEntities;
import xyz.notagardener.status.common.service.StatusCommandService;
import xyz.notagardener.status.plant.dto.AddStatusResponse;
import xyz.notagardener.status.plant.dto.PlantStatusRequest;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantStatusServiceImpl implements PlantStatusService {
    private final StatusCommandService statusCommandService;

    @Override
    @Transactional
    public AddStatusResponse add(PlantStatusRequest request, Long gardenerId) {
        StatusEntities entities = statusCommandService.add(request, gardenerId);
        return new AddStatusResponse(entities);
    }

    @Override
    @Transactional
    public PlantStatusResponse update(PlantStatusRequest request, Long gardenerId) {
        StatusEntities entities = statusCommandService.update(request, gardenerId);
        return new PlantStatusResponse(entities.getStatus());
    }

    @Override
    @Transactional
    public PlantStatusResponse delete(Long plantId, Long statusId, Long gardenerId) {
        Status status = statusCommandService.delete(statusId, gardenerId);
        return new PlantStatusResponse(status);
    }
}
