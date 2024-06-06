package xyz.notagardener.status.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.repository.PlantStatusRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantStatusQueryServiceImpl implements PlantStatusQueryService {
    private final PlantStatusRepository plantStatusRepository;

    @Override
    public List<PlantStatusResponse> getRecentStatusByPlantId(Long plantId, Long gardenerId) {
        return plantStatusRepository.findCurrentStatusByPlantId(plantId)
                .stream()
                .filter(status -> YesOrNoType.Y.equals(status.getActive())) // TODO 임시 조건
                .map(PlantStatusResponse::new)
                .toList();
    }
}
