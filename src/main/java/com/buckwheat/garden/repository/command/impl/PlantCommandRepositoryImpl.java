package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.data.dto.place.ModifyPlace;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.dao.PlaceDao;
import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.repository.command.PlantCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlantCommandRepositoryImpl implements PlantCommandRepository {
    private final PlantDao plantDao;
    private final PlaceDao placeDao;
    private final GardenerDao gardenerDao;

    @Override
    public Plant save(Long gardenerId, PlantRequest plantRequest) {
        Place place = placeDao.findByPlaceIdAndGardener_GardenerId(plantRequest.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);
        Gardener gardener = gardenerDao.getReferenceById(gardenerId);

        return plantDao.save(plantRequest.toEntityWith(gardener, place));
    }

    @Override
    @Transactional
    public Plant update(PlantRequest plantRequest, Long gardenerId) {
        Place place = placeDao.findByPlaceIdAndGardener_GardenerId(plantRequest.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        Plant plant = plantDao.findByPlantIdAndGardener_GardenerId(plantRequest.getId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);
        plant.update(plantRequest, place);

        return plant;
    }

    @Override
    @Transactional
    public Place updatePlantPlace(ModifyPlace modifyPlantPlace, Long gardenerId) {
        Place place = placeDao.findByPlaceIdAndGardener_GardenerId(modifyPlantPlace.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        for (Long plantId : modifyPlantPlace.getPlants()) {
            Plant plant = plantDao.findById(plantId).orElseThrow(NoSuchElementException::new);
            plant.updatePlace(place);
        }

        return place;
    }

    @Override
    public void deleteBy(Long id, Long gardenerId) {
        plantDao.deleteById(id);
    }
}
