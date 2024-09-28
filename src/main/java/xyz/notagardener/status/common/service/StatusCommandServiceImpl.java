package xyz.notagardener.status.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.status.common.model.Status;
import xyz.notagardener.status.common.model.StatusEntities;
import xyz.notagardener.status.common.model.StatusLog;
import xyz.notagardener.status.common.repository.StatusLogRepository;
import xyz.notagardener.status.common.repository.StatusRepository;
import xyz.notagardener.status.plant.dto.PlantStatusRequest;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatusCommandServiceImpl implements StatusCommandService {
    private final StatusRepository statusRepository;
    private final StatusLogRepository statusLogRepository;
    private final PlantRepository plantRepository;

    @Override
    @Transactional
    public StatusEntities add(PlantStatusRequest request, Long gardenerId) {
        Plant plant = getPlantByPlantIdAndGardenerId(request.getPlantId(), gardenerId);
        Optional<Status> prevStatus = statusRepository.findByPlant_PlantId(request.getPlantId());

        if (prevStatus.isEmpty()) {
            Status savedStatus = statusRepository.save(request.toStatusWith(plant));
            StatusLog statusLog = statusLogRepository.save(request.toStatusLogWith(savedStatus)); // update log table
            return new StatusEntities(savedStatus, statusLog);
        }

        // 있는 거 수정
        Status status = prevStatus.get();
        status.update(request.getStatusType(), request.getActive());
        StatusLog statusLog = statusLogRepository.save(request.toStatusLogWith(status)); // update log table

        return new StatusEntities(status, statusLog);
    }

    @Override
    @Transactional
    public StatusEntities update(PlantStatusRequest request, Long gardenerId) {
        Status status = getStatusByIdAndGardenerId(request.getPlantStatusId(), gardenerId);
        Plant plant = getPlantByPlantIdAndGardenerId(request.getPlantId(), gardenerId);

        status.update(request.getStatusType(), request.getActive(), plant);

        // 로그 테이블 업데이트
        StatusLog statusLog = statusLogRepository.save(request.toStatusLogWith(status));

        return new StatusEntities(status, statusLog);
    }

    @Transactional
    public Status delete(Long statusId, Long gardenerId) {
        Status status = getStatusByIdAndGardenerId(statusId, gardenerId);

        // 로그 다 지우기
        statusLogRepository.deleteByStatus_StatusId(statusId);

        // 식물 상태 초기화
        status.init();

        return status;
    }

    @Transactional(readOnly = true)
    private Plant getPlantByPlantIdAndGardenerId(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT));

        if (!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT);
        }

        return plant;
    }

    @Transactional
    private Status getStatusByIdAndGardenerId(Long plantStatusId, Long gardenerId) {
        Status status = statusRepository.findByStatusId(plantStatusId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT_STATUS));

        if (!status.getPlant().getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT_STATUS);
        }

        return status;
    }
}
