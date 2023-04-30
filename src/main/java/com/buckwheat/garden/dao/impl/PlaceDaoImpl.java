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
    public Place getPlaceWithPlants(Long placeId) {
        return placeRepository.findByPlaceId(placeId).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Transactinal 필요
     *
     * @param placeRequest
     * @return
     */
    @Override
    public Place save(Long gardenerId, PlaceDto.Request placeRequest) {
        Gardener gardener = gardenerRepository.findById(gardenerId).orElseThrow(NoSuchElementException::new);
        return placeRepository.save(placeRequest.toEntityWith(gardener));
    }

    @Override
    public Place update(PlaceDto.Request placeRequest) {
        Place place = placeRepository.findById(placeRequest.getId()).orElseThrow(NoSuchElementException::new);

        return placeRepository.save(
                place.update(
                        placeRequest.getName(),
                        placeRequest.getOption(),
                        placeRequest.getArtificialLight()
                )
        );
    }

    @Override
    public void deleteBy(Long id) {
        placeRepository.deleteById(id);
    }
}
