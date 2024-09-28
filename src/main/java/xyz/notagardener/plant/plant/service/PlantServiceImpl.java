package xyz.notagardener.plant.plant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.place.model.Place;
import xyz.notagardener.place.dto.ModifyPlace;
import xyz.notagardener.place.dto.PlaceDto;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.garden.service.GardenResponseMapperImpl;
import xyz.notagardener.plant.plant.dto.PlantBasic;
import xyz.notagardener.plant.plant.dto.PlantRequest;
import xyz.notagardener.plant.plant.repository.PlantRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {
    private final PlantCommandService plantCommandService;
    private final PlantRepository plantRepository;
    private final GardenResponseMapperImpl gardenResponseMapper;

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
    public List<PlantResponse> getAll(Long gardenerId) {
        return plantRepository.findByGardener_GardenerIdOrderByPlantIdDesc(gardenerId).stream()
                .map(PlantResponse::new)
                .toList();
    }

    @Override
    public List<PlantBasic> getAttentionNotRequiredPlants(Long gardenerId) {
        return plantRepository.findAttentionNotRequiredPlants(gardenerId);
    }

    @Override
    @Transactional(readOnly = true)
    public PlantResponse getDetail(Long plantId, Long gardenerId) {
        return plantRepository.findPlantWithLatestWateringDate(plantId, gardenerId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT));
    }

    @Override
    @Transactional
    public GardenResponse add(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantCommandService.save(gardenerId, plantRequest);
        return gardenResponseMapper.getGardenResponse(new PlantResponse(plant), gardenerId);
    }

    @Override
    @Transactional
    public GardenResponse update(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantCommandService.update(plantRequest, gardenerId);
        return gardenResponseMapper.getGardenResponse(new PlantResponse(plant), gardenerId);
    }

    @Override
    @Transactional
    public PlaceDto updatePlace(ModifyPlace modifyPlace, Long gardenerId) {
        Place place = plantCommandService.updatePlantPlace(modifyPlace, gardenerId);
        return new PlaceDto(place);
    }

    @Override
    public void delete(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT));

        if (!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT);
        }

        plantRepository.delete(plant);
    }
}
