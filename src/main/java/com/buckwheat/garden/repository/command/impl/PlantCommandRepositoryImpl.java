package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.repository.command.PlantCommandRepository;
import com.buckwheat.garden.data.dto.place.ModifyPlace;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.GardenerRepository;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlantCommandRepositoryImpl implements PlantCommandRepository {
    private final PlantRepository plantRepository;
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;
    private final WateringRepository wateringRepository;

    @Override
    public Plant save(Long gardenerId, PlantRequest plantRequest) {
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(plantRequest.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        return plantRepository.save(plantRequest.toEntityWith(gardener, place));
    }

    @Override
    @Transactional
    public Plant update(PlantRequest plantRequest, Long gardenerId){
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(plantRequest.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        Plant plant = plantRepository.findByPlantIdAndGardener_GardenerId(plantRequest.getId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        return plant.update(plantRequest, place);
    }

    @Override
    @Transactional
    public Place updatePlantPlace(ModifyPlace modifyPlantPlace, Long gardenerId){
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(modifyPlantPlace.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        for (Long plantId : modifyPlantPlace.getPlants()) {
            Plant plant = plantRepository.findById(plantId).orElseThrow(NoSuchElementException::new);
            plant.updatePlace(place);
        }

        return place;
    }

    @Override
    public void deleteBy(Long id, Long gardenerId){
        plantRepository.deleteById(id);
    }

    @Override
    public LocalDate getLatestWateringDate(Long plantId) {
        return wateringRepository.findLatestWateringDate(plantId);
    }
}
