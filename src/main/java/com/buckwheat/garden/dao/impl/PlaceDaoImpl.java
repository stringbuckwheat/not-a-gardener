package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.PlaceDao;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.repository.GardenerRepository;
import com.buckwheat.garden.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class PlaceDaoImpl implements PlaceDao {
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;

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

    /**
     * @param placeRequest
     * @return
     */
    @Override
    public Place save(Long gardenerId, PlaceDto.Basic placeRequest) {
        Gardener gardener = gardenerRepository.findById(gardenerId).orElseThrow(NoSuchElementException::new);
        return placeRepository.save(placeRequest.toEntityWith(gardener));
    }

    @Override
    public Place update(PlaceDto.Basic placeRequest, Long gardenerId) {
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(placeRequest.getId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        return placeRepository.save(
                place.update(
                        placeRequest.getName(),
                        placeRequest.getOption(),
                        placeRequest.getArtificialLight()
                )
        );
    }

    @Override
    public void deleteBy(Long placeId, Long gardenerId) {
        placeRepository.deleteById(placeId);
    }
}
