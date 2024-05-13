package xyz.notagardener.domain.plant.service;

import xyz.notagardener.domain.gardener.Gardener;
import xyz.notagardener.domain.place.Place;
import xyz.notagardener.domain.gardener.repository.GardenerRepository;
import xyz.notagardener.domain.place.PlaceRepository;
import xyz.notagardener.domain.place.dto.ModifyPlace;
import xyz.notagardener.domain.plant.Plant;
import xyz.notagardener.domain.plant.repository.PlantRepository;
import xyz.notagardener.domain.plant.dto.plant.PlantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlantCommandServiceImpl implements PlantCommandService {
    private final PlantRepository plantRepository;
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional
    public <T> T save(Long gardenerId, PlantRequest plantRequest, ServiceCallBack<T> callBack) {
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(plantRequest.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        Plant plant = plantRepository.save(plantRequest.toEntityWith(gardener, place));

        return callBack.execute(plant);
    }

    @Override
    @Transactional
    public <T> T update(PlantRequest plantRequest, Long gardenerId, ServiceCallBack<T> callBack) {
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(plantRequest.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        Plant plant = plantRepository.findByPlantIdAndGardener_GardenerId(plantRequest.getId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);
        plant.update(plantRequest, place);

        return callBack.execute(plant);
    }

    @Override
    @Transactional
    public Place updatePlantPlace(ModifyPlace modifyPlantPlace, Long gardenerId) {
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(modifyPlantPlace.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        for (Long plantId : modifyPlantPlace.getPlants()) {
            Plant plant = plantRepository.findById(plantId).orElseThrow(NoSuchElementException::new);
            plant.updatePlace(place);
        }

        return place;
    }
}
