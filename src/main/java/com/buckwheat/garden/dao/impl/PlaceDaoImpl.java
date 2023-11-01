package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.PlaceDao;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.GardenerRepository;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class PlaceDaoImpl implements PlaceDao {
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;
    private final PlantRepository plantRepository;

    /**
     * @param gardenerId
     * @return
     * @Transactional X, EntityGraph로 한 번에 조회
     */
    @Override
    public List<Place> getPlacesByGardenerId(Long gardenerId) {
        return placeRepository.findByGardener_GardenerIdOrderByCreateDate(gardenerId);
    }

    @Override
    public Place getPlaceWithPlants(Long placeId, Long gardenerId) {
        return placeRepository.findByPlaceIdAndGardener_GardenerId(placeId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public int countPlantsInPlace(Long placeId) {
        return plantRepository.countByPlace_PlaceId(placeId);
    }

    @Override
    public List<Plant> getPlantsInPlace(Long placeId, Pageable pageable) {
        return plantRepository.findPlantsByPlaceIdWithPage(placeId, pageable);
    }

    /**
     * @param placeRequest
     * @return
     */
    @Override
    public Place save(Long gardenerId, PlaceDto placeRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        return placeRepository.save(placeRequest.toEntityWith(gardener));
    }

    @Override
    @Transactional
    public Place update(PlaceDto placeRequest, Long gardenerId) {
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(placeRequest.getId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        place.update(
                placeRequest.getName(),
                placeRequest.getOption(),
                placeRequest.getArtificialLight()
        );

        return place;
    }

    @Override
    public void deleteBy(Long placeId, Long gardenerId) {
        placeRepository.deleteById(placeId);
    }
}
