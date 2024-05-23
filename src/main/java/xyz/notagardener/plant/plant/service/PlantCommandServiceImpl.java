package xyz.notagardener.plant.plant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.place.Place;
import xyz.notagardener.place.repository.PlaceRepository;
import xyz.notagardener.place.dto.ModifyPlace;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.dto.PlantRequest;
import xyz.notagardener.plant.plant.repository.PlantRepository;

@Service
@RequiredArgsConstructor
public class PlantCommandServiceImpl implements PlantCommandService {
    private final PlantRepository plantRepository;
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;

    private Place getPlaceByPlaceIdAndGardenerId(Long placeId, Long gardenerId) {
        Place place = placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLACE));

        if (!place.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLACE);
        }

        return place;
    }

    private Plant getPlantByPlantIdAndGardenerId(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT));

        if(!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT);
        }

        return plant;
    }


    @Override
    @Transactional
    public Plant save(Long gardenerId, PlantRequest plantRequest) {
        Place place = getPlaceByPlaceIdAndGardenerId(plantRequest.getPlaceId(), gardenerId);
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        return plantRepository.save(plantRequest.toEntityWith(gardener, place));
    }

    @Override
    @Transactional
    public Plant update(PlantRequest plantRequest, Long gardenerId) {
        Place place = getPlaceByPlaceIdAndGardenerId(plantRequest.getPlaceId(), gardenerId);

        Plant plant = getPlantByPlantIdAndGardenerId(plantRequest.getId(), gardenerId);
        plant.update(plantRequest, place);

        return plant;
    }

    @Override
    @Transactional
    public Place updatePlantPlace(ModifyPlace modifyPlantPlace, Long gardenerId) {
        Place place = getPlaceByPlaceIdAndGardenerId(modifyPlantPlace.getPlaceId(), gardenerId);

        for (Long plantId : modifyPlantPlace.getPlants()) {
            Plant plant = getPlantByPlantIdAndGardenerId(plantId, gardenerId);
            plant.updatePlace(place);
        }

        return place;
    }
}
