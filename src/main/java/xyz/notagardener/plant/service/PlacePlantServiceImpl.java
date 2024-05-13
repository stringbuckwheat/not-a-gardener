package xyz.notagardener.domain.plant.service;

import xyz.notagardener.domain.plant.dto.plant.PlantInPlace;
import xyz.notagardener.domain.plant.dto.plant.PlantRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlacePlantServiceImpl implements PlacePlantService {
    private final PlantCommandService plantCommandService;

    @Override
    @Transactional
    public PlantInPlace addPlantInPlace(Long gardenerId, PlantRequest plantRequest) {
        return plantCommandService.save(gardenerId, plantRequest, PlantInPlace::from);
    }
}
