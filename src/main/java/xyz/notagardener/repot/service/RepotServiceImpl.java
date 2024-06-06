package xyz.notagardener.repot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.dto.RepotList;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.repot.Repot;
import xyz.notagardener.repot.dto.RepotRequest;
import xyz.notagardener.repot.dto.RepotResponse;
import xyz.notagardener.repot.repository.RepotRepository;
import xyz.notagardener.status.dto.PlantStatusResponse;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepotServiceImpl implements RepotService {
    private final PlantRepository plantRepository;
    private final RepotRepository repotRepository;
    private final RepotStatusService repotStatusService;

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
    private Repot getRepotByRepotIdAndGardenerId(Long repotId, Long gardenerId) {
        Repot repot = repotRepository.findByRepotId(repotId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_REPOT));

        if (!repot.getPlant().getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_REPOT);
        }

        return repot;
    }

    // 한 개 분갈이
    @Override
    @Transactional
    public RepotResponse addOne(RepotRequest repotRequest, Long gardenerId) {
        Plant plant = getPlantByPlantIdAndGardenerId(repotRequest.getPlantId(), gardenerId);
        Repot repot = repotRepository.save(new Repot(repotRequest.getRepotDate(), plant, repotRequest.getHaveToInitPeriod()));

        // 물주기 기록 초기화 or 유지
        if (YesOrNoType.Y.equals(repotRequest.getHaveToInitPeriod())) {
            plant.updateRecentWateringPeriod(0);
            plant.updateEarlyWateringPeriod(0);
        }

        PlantStatusResponse status = repotStatusService.handleRepotStatus(repotRequest, plant);

        return new RepotResponse(repot, status);
    }

    // 여러 개 한 번에 분갈이
    @Override
    @Transactional
    public List<RepotResponse> addAll(List<RepotRequest> requests, Long gardenerId) {
        return requests.stream()
                .map((request) -> addOne(request, gardenerId))
                .toList();
    }

    @Override
    public List<RepotList> getAll(Long gardenerId, Pageable pageable) {
        return repotRepository.findAll(gardenerId, pageable);
    }

    // TODO 물주기 캘린더 -> 정원기록 모듈
    // TODO 식물별 기록은 식물 서비스로 보내기 -> 식물 로그

    @Transactional
    @Override
    public RepotResponse update(RepotRequest repotRequest, Long gardenerId) {
        Repot repot = getRepotByRepotIdAndGardenerId(repotRequest.getRepotId(), gardenerId);
        PlantStatusResponse status = repotStatusService.handleRepotStatus(repotRequest, repot.getPlant());

        // 데이터 수정
        repot.update(repotRequest.getRepotDate());
        return new RepotResponse(repot, status);
    }

    @Override
    public List<RepotResponse> updateAll(List<RepotRequest> repotRequests, Long gardenerId) {
        return repotRequests.stream().map((request) -> update(request, gardenerId)).toList();
    }

    @Override
    public void delete(Long repotId, Long gardenerId) {
        Repot repot = getRepotByRepotIdAndGardenerId(repotId, gardenerId);
        repotRepository.delete(repot);
    }
}
