package xyz.notagardener.repot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.repot.dto.RepotRequest;
import xyz.notagardener.status.PlantStatus;
import xyz.notagardener.status.repository.PlantStatusRepository;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.watering.watering.repository.WateringRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepotStatusServiceImpl implements RepotStatusService{
    private final WateringRepository wateringRepository;
    private final PlantStatusRepository plantStatusRepository;

    @Override
    public PlantStatusResponse handleRepotStatus(RepotRequest request, Plant plant) {
        // 분갈이 날짜 이후 물주기 기록 있음
        if(wateringRepository.existsByPlant_PlantIdAndWateringDateAfter(request.getPlantId(), request.getRepotDate())) {
            // PlantStatus 추가하지 않음
            return new PlantStatusResponse();
        }

        return addRepotStatus(request, plant);
    }

    private PlantStatusResponse addRepotStatus(RepotRequest request, Plant plant) {
        // 분갈이 STATUS 추가
        PlantStatus status = plantStatusRepository.save(request.toPlantStatusWith(plant));
        return new PlantStatusResponse(status);
    }
}
