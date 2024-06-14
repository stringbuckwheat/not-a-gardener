package xyz.notagardener.status.garden;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.notagardener.plant.garden.dto.AttentionRequiredPlant;
import xyz.notagardener.status.common.model.StatusEntities;
import xyz.notagardener.status.common.service.StatusCommandService;
import xyz.notagardener.status.plant.dto.PlantStatusRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class GardenStatusServiceImpl implements GardenStatusService{
    private final StatusCommandService statusCommandService;

    @Override
    public AttentionRequiredPlant add(PlantStatusRequest request, Long gardenerId) {
        StatusEntities entities = statusCommandService.add(request, gardenerId);
        return new AttentionRequiredPlant(entities.getStatus());
    }
}
