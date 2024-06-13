package xyz.notagardener.repot.repot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.repot.Repot;
import xyz.notagardener.repot.repot.dto.RepotRequest;
import xyz.notagardener.repot.repot.repository.RepotRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepotCommandServiceImpl implements RepotCommandService {
    private final RepotRepository repotRepository;
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

    @Override
    @Transactional(readOnly = true)
    public Repot getRepotByRepotIdAndGardenerId(Long repotId, Long gardenerId) {
        Repot repot = repotRepository.findByRepotId(repotId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_REPOT));

        if (!repot.getPlant().getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_REPOT);
        }

        return repot;
    }

    @Override
    @Transactional
    public Repot addOne(RepotRequest repotRequest, Long gardenerId) {
        Plant plant = getPlantByPlantIdAndGardenerId(repotRequest.getPlantId(), gardenerId);
        Repot repot = repotRepository.save(new Repot(repotRequest.getRepotDate(), plant, repotRequest.getHaveToInitPeriod()));

        // 물주기 기록 초기화 or 유지
        if (YesOrNoType.Y.equals(repotRequest.getHaveToInitPeriod())) {
            plant.updateRecentWateringPeriod(0);
            plant.updateEarlyWateringPeriod(0);
        }

        return repot;
    }

    @Override
    @Transactional
    public Repot update(RepotRequest repotRequest, Long gardenerId) {
        Repot repot = getRepotByRepotIdAndGardenerId(repotRequest.getRepotId(), gardenerId);

        // 데이터 수정
        repot.update(repotRequest.getRepotDate());
        return repot;
    }
}
