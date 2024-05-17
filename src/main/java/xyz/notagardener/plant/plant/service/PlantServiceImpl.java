package xyz.notagardener.plant.plant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.place.Place;
import xyz.notagardener.place.dto.ModifyPlace;
import xyz.notagardener.place.dto.PlaceDto;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.garden.service.GardenResponseMapperImpl;
import xyz.notagardener.plant.plant.dto.PlantRequest;
import xyz.notagardener.plant.plant.repository.PlantRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {
    private final PlantCommandService plantCommandService;
    private final PlantRepository plantRepository;
    private final GardenResponseMapperImpl gardenResponseMapper;

    private Plant getPlantByPlantIdAndGardenerId(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionCode.NO_SUCH_ITEM.getCode()));

        if (!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException("PLANT", gardenerId);
        }

        return plant;
    }

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
        return plantRepository.findPlantWithLatestWateringDate(plantId, gardenerId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionCode.NO_SUCH_ITEM.getCode()));
    }

    @Override
    @Transactional
    public GardenResponse add(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantCommandService.save(gardenerId, plantRequest);
        return gardenResponseMapper.getGardenResponse(PlantResponse.from(plant), gardenerId);
    }

    @Override
    @Transactional
    public GardenResponse update(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantCommandService.update(plantRequest, gardenerId);
        return gardenResponseMapper.getGardenResponse(PlantResponse.from(plant), gardenerId);
    }

    @Override
    @Transactional
    public PlaceDto updatePlace(ModifyPlace modifyPlace, Long gardenerId) {
        Place place = plantCommandService.updatePlantPlace(modifyPlace, gardenerId);
        return PlaceDto.from(place);
    }

    @Override
    public void delete(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId).orElseThrow(NoSuchElementException::new);

        if (!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException("PLANT", gardenerId);
        }

        plantRepository.delete(plant);
    }
}
