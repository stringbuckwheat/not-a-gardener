package xyz.notagardener.domain.plant.service;

import xyz.notagardener.domain.place.Place;
import xyz.notagardener.domain.place.dto.ModifyPlace;
import xyz.notagardener.domain.place.dto.PlaceDto;
import xyz.notagardener.domain.plant.dto.projection.Calculate;
import xyz.notagardener.domain.plant.Plant;
import xyz.notagardener.domain.plant.repository.PlantRepository;
import xyz.notagardener.domain.plant.dto.garden.GardenResponse;
import xyz.notagardener.domain.plant.dto.plant.PlantRequest;
import xyz.notagardener.domain.plant.dto.plant.PlantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {
    private final PlantCommandService plantCommandService;
    private final PlantRepository plantRepository;
    private final GardenResponseMapper gardenResponseMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PlantResponse> getAll(Long gardenerId) {
        return plantRepository.findByGardener_GardenerIdOrderByPlantIdDesc(gardenerId).stream()
                .map(PlantResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlantResponse getDetail(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantIdAndGardener_GardenerId(plantId, gardenerId)
                .orElseThrow(NoSuchElementException::new);

        Long totalWaterings = plantRepository.countWateringByPlantId(plantId);
        LocalDate latestWateringDate = plantRepository.findLatestWateringDateByPlantId(plantId);

        return PlantResponse.from(plant, totalWaterings, latestWateringDate);
    }

    @Override
    @Transactional
    public GardenResponse add(Long gardenerId, PlantRequest plantRequest) {
        return plantCommandService.save(gardenerId, plantRequest, plant ->
                gardenResponseMapper.getGardenResponse(Calculate.from(plant, gardenerId)));
    }

    @Override
    @Transactional
    public GardenResponse update(Long gardenerId, PlantRequest plantRequest) {
        return plantCommandService.update(plantRequest, gardenerId, plant ->
                gardenResponseMapper.getGardenResponse(Calculate.from(plant, gardenerId)));
    }

    @Override
    @Transactional
    public PlaceDto updatePlace(ModifyPlace modifyPlace, Long gardenerId) {
        Place place = plantCommandService.updatePlantPlace(modifyPlace, gardenerId);
        return PlaceDto.from(place);
    }

    @Override
    public void delete(Long plantId, Long gardenerId) {
        plantRepository.deleteById(plantId);
    }
}
