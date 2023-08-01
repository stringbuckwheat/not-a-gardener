package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.projection.RawGarden;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.GardenerRepository;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlantDaoImpl implements PlantDao {
    private final PlantRepository plantRepository;
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    public List<RawGarden> getGarden(Long gardenerId) {
        return plantRepository.findGardenByGardenerId(gardenerId);
    }

    @Override
    public List<Plant> getWaitingForWateringList(Long gardenerId) {
        return plantRepository.findWaitingForWateringList(gardenerId);
    }

    @Override
    public List<Plant> getPlantsByGardenerId(Long gardenerId){
        return plantRepository.findByGardener_GardenerIdOrderByCreateDateDesc(gardenerId);
    }

    @Override
    public Plant getPlantWithPlaceAndWatering(Long id) {
        return plantRepository.findByPlantId(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Plant getPlantWithPlantIdAndGardenerId(Long plantId, Long gardenerId){
        return plantRepository.findByPlantIdAndGardener_GardenerId(plantId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Plant> getPlantsForGarden(Long gardenerId){
        return plantRepository.findByGardener_GardenerId(gardenerId);
    }

    @Override
    public Plant save(Long gardenerId, PlantDto.Request plantRequest) {
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(plantRequest.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        return plantRepository.save(plantRequest.toEntityWith(gardener, place));
    }

    @Override
    public Plant update(PlantDto.Request plantRequest, Long gardenerId){
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(plantRequest.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        Plant plant = plantRepository.findByPlantIdAndGardener_GardenerId(plantRequest.getId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        return plantRepository.save(plant.update(plantRequest, place));
    }

    @Override
    public Plant update(Plant plant){
        return plantRepository.save(plant);
    }

    @Override
    public Place updatePlantPlace(PlaceDto.ModifyPlace modifyPlantPlace, Long gardenerId){
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(modifyPlantPlace.getPlaceId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        for (Long plantId : modifyPlantPlace.getPlantList()) {
            Plant plant = plantRepository.findById(plantId).orElseThrow(NoSuchElementException::new);

            plant.updatePlace(place);
            plantRepository.save(plant);
        }

        return place;
    }

    public void updateConditionDate(Plant plant){
        plantRepository.save(plant.updateConditionDate());
    }

    @Override
    public void deleteBy(Long id, Long gardenerId){
        plantRepository.deleteById(id);
    }
}
