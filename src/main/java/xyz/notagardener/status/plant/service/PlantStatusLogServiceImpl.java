package xyz.notagardener.status.plant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;
import xyz.notagardener.status.plant.dto.StatusLogResponse;
import xyz.notagardener.status.common.model.StatusLog;
import xyz.notagardener.status.common.repository.StatusLogRepository;
import xyz.notagardener.status.common.repository.StatusRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantStatusLogServiceImpl implements PlantStatusLogService {
    private final StatusLogRepository statusLogRepository;
    private final StatusRepository statusRepository;

    private StatusLog getStatusLogByStatusLogIdAndGardenerId(Long statusLogId, Long gardenerId) {
        StatusLog statusLog = statusLogRepository.findByStatusLogId(statusLogId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NOT_YOUR_PLANT_STATUS_LOG));

        if (!statusLog.getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT_STATUS_LOG);
        }

        return statusLog;
    }

    @Override
    public List<StatusLogResponse> getAllLog(Long plantId, Long gardenerId) {
        return statusLogRepository.findAllStatusLogByStatus_Plant_PlantIdAndStatus_Plant_Gardener_GardenerIdOrderByRecordedDateDescCreateDateDesc(plantId, gardenerId)
                .stream().map(StatusLogResponse::new).toList();
    }

    @Override
    @Transactional
    public PlantStatusResponse deleteOne(Long statusLogId, Long gardenerId) {
        StatusLog statusLog = getStatusLogByStatusLogIdAndGardenerId(statusLogId, gardenerId);

        Long plantId = statusLog.getStatus().getPlant().getPlantId();

        // 해당 StatusType의 가장 최신값(유효한 값) 가져오기
        Optional<StatusLog> latestStatusLog = statusLogRepository.findTopByStatus_Plant_PlantIdAndStatusTypeOrderByRecordedDateDescCreateDateDesc(plantId, statusLog.getStatusType());
        log.debug("found latestStatusLog");

        // 일치 시 Status 업데이트
        if(latestStatusLog.isPresent() && latestStatusLog.get().getStatusLogId().equals(statusLogId)) {
            YesOrNoType active = statusLog.getActive().equals(YesOrNoType.Y) ? YesOrNoType.N : YesOrNoType.Y;
            statusLog.getStatus().update(statusLog.getStatusType(), active);
        }

        // 로그 삭제
        statusLogRepository.delete(statusLog);

        return new PlantStatusResponse(statusLog.getStatus());
    }
}
