package xyz.notagardener.plant.plant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
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
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.service.PlantStatusQueryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {
    private final PlantCommandService plantCommandService;
    private final PlantRepository plantRepository;
    private final PlantStatusQueryService plantStatusQueryService;
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
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlantResponse getDetail(Long plantId, Long gardenerId) {
        PlantResponse plant = plantRepository.findPlantWithLatestWateringDate(plantId, gardenerId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT));
        plant.setStatus(plantStatusQueryService.getRecentStatusByPlantId(plantId, gardenerId));

        return plant;
    }

    @Override
    @Transactional
    public GardenResponse add(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantCommandService.save(gardenerId, plantRequest);
        List<PlantStatusResponse> status = plantStatusQueryService.getRecentStatusByPlantId(plantRequest.getId(), gardenerId);
        return gardenResponseMapper.getGardenResponse(new PlantResponse(plant, status), gardenerId);
    }

    @Override
    @Transactional
    public GardenResponse update(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantCommandService.update(plantRequest, gardenerId);
        List<PlantStatusResponse> status = plantStatusQueryService.getRecentStatusByPlantId(plantRequest.getId(), gardenerId);
        return gardenResponseMapper.getGardenResponse(new PlantResponse(plant, status), gardenerId);
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
