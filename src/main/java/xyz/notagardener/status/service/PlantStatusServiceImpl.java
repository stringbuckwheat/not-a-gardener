package xyz.notagardener.status.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.status.PlantStatus;
import xyz.notagardener.status.dto.PlantStatusRequest;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.repository.PlantStatusRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantStatusServiceImpl implements PlantStatusService {
    private final PlantStatusQueryService plantStatusQueryService;
    private final PlantStatusRepository plantStatusRepository;
    private final PlantRepository plantRepository;

    @Transactional(readOnly = true)
    private Plant getPlantByPlantIdAndGardenerId(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT));

        if (!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT);
        }

        return plant;
    }

    @Transactional(readOnly = true)
    private PlantStatus getPlantStatusByPlantStatusIdAndGardenerId(Long plantStatusId, Long gardenerId) {
        PlantStatus status = plantStatusRepository.findByPlantStatusId(plantStatusId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT_STATUS));

        if (!status.getPlant().getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT_STATUS);
        }

        return status;
    }

    @Override
    public PlantStatusResponse add(PlantStatusRequest request, Long gardenerId) {
        Plant plant = getPlantByPlantIdAndGardenerId(request.getPlantId(), gardenerId);
        PlantStatus status = plantStatusRepository.save(request.toEntityWith(plant));

        return new PlantStatusResponse(status);
    }

    @Override
    public List<PlantStatusResponse> getAll(Long plantId, Long gardenerId) {
        return plantStatusRepository.findAllStatusByPlant_PlantIdAndPlant_Gardener_GardenerIdOrderByRecordedDateDescCreateDateDesc(plantId, gardenerId)
                .stream().map(PlantStatusResponse::new).toList();
    }

    @Override
    public PlantStatusResponse update(PlantStatusRequest request, Long gardenerId) {
        PlantStatus status = getPlantStatusByPlantStatusIdAndGardenerId(request.getPlantStatusId(), gardenerId);
        Plant plant = getPlantByPlantIdAndGardenerId(request.getPlantId(), gardenerId);

        status.update(request.getStatus(), request.getRecordedDate(), plant);

        return new PlantStatusResponse(status);
    }

    @Override
    public List<PlantStatusResponse> delete(Long plantId, Long plantStatusId, Long gardenerId) {
        PlantStatus status = getPlantStatusByPlantStatusIdAndGardenerId(plantStatusId, gardenerId);
        plantStatusRepository.delete(status);

        return plantStatusQueryService.getRecentStatusByPlantId(plantId, gardenerId);
    }
}
