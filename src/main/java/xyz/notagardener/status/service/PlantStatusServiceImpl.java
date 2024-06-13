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
import xyz.notagardener.status.dto.AddStatusResponse;
import xyz.notagardener.status.dto.PlantStatusRequest;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.model.Status;
import xyz.notagardener.status.model.StatusLog;
import xyz.notagardener.status.repository.StatusLogRepository;
import xyz.notagardener.status.repository.StatusRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantStatusServiceImpl implements PlantStatusService {
    private final StatusRepository statusRepository;
    private final StatusLogRepository statusLogRepository;
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
    private Status getStatusByIdAndGardenerId(Long plantStatusId, Long gardenerId) {
        Status status = statusRepository.findByStatusId(plantStatusId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT_STATUS));

        if (!status.getPlant().getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT_STATUS);
        }

        return status;
    }

    @Override
    @Transactional
    public AddStatusResponse add(PlantStatusRequest request, Long gardenerId) {
        Plant plant = getPlantByPlantIdAndGardenerId(request.getPlantId(), gardenerId);
        Optional<Status> prevStatus = statusRepository.findByPlant_PlantId(request.getPlantId());

        if (prevStatus.isEmpty()) {
            Status savedStatus = statusRepository.save(request.toStatusWith(plant));
            StatusLog statusLog = statusLogRepository.save(request.toStatusLogWith(savedStatus)); // update log table
            return new AddStatusResponse(savedStatus, statusLog);
        }

        // 있는 거 수정
        Status status = prevStatus.get();
        status.update(request.getStatusType(), request.getActive());
        StatusLog statusLog = statusLogRepository.save(request.toStatusLogWith(status)); // update log table

        return new AddStatusResponse(status, statusLog);
    }

    @Override
    @Transactional
    public PlantStatusResponse update(PlantStatusRequest request, Long gardenerId) {
        Status status = getStatusByIdAndGardenerId(request.getPlantStatusId(), gardenerId);
        Plant plant = getPlantByPlantIdAndGardenerId(request.getPlantId(), gardenerId);

        status.update(request.getStatusType(), request.getActive(), plant);

        // 로그 테이블 업데이트
        statusLogRepository.save(request.toStatusLogWith(status));

        return new PlantStatusResponse(status);
    }

    @Override
    @Transactional
    public PlantStatusResponse delete(Long plantId, Long statusId, Long gardenerId) {
        Status status = getStatusByIdAndGardenerId(statusId, gardenerId);

        // 로그 다 지우기
        statusLogRepository.deleteByStatus_StatusId(statusId);

        // 식물 상태 초기화
        status.init();

        return new PlantStatusResponse(status);
    }
}
