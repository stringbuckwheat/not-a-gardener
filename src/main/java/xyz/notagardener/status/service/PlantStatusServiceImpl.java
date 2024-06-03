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

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantStatusServiceImpl implements PlantStatusService {
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

    // CREATE
    @Override
    public PlantStatusResponse add(PlantStatusRequest request, Long gardenerId) {
        Plant plant = getPlantByPlantIdAndGardenerId(request.getPlantId(), gardenerId);
        PlantStatus status = plantStatusRepository.save(request.toEntityWith(plant));

        return new PlantStatusResponse(status);
    }

    // READ

    // UPDATE
    @Override
    public PlantStatusResponse update(PlantStatusRequest request, Long gardenerId) {
        // TODO 유니크 제약조건 위배시 처리 고민
        //// 1) UPDATE 불가 엔티티로 만들기
        //// 2) AOP 추가하기

        PlantStatus status = getPlantStatusByPlantStatusIdAndGardenerId(request.getPlantStatusId(), gardenerId);
        Plant plant = getPlantByPlantIdAndGardenerId(request.getPlantId(), gardenerId);

        status.update(request.getStatus(), request.getRecordedDate(), plant);

        return new PlantStatusResponse(status);
    }

    // DELETE
    @Override
    public void delete(Long plantStatusId, Long gardenerId) {
        PlantStatus status = getPlantStatusByPlantStatusIdAndGardenerId(plantStatusId, gardenerId);
        plantStatusRepository.delete(status);
    }
}
