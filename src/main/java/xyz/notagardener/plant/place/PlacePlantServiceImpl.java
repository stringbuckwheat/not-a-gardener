package xyz.notagardener.plant.place;

import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.dto.PlantInPlace;
import xyz.notagardener.plant.plant.dto.PlantRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.plant.plant.service.PlantCommandService;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlacePlantServiceImpl implements PlacePlantService {
    private final PlantCommandService plantCommandService;

    @Override
    @Transactional
    public PlantInPlace addPlantInPlace(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantCommandService.save(gardenerId, plantRequest);
        return new PlantInPlace(plant);
    }
}
