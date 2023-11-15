package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.repository.command.PlaceCommandRepository;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.repository.dao.GardenerDao;
import com.buckwheat.garden.repository.dao.PlaceDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class PlaceCommandRepositoryImpl implements PlaceCommandRepository {
    private final PlaceDao placeDao;
    private final GardenerDao gardenerDao;

    @Override
    public Place save(Long gardenerId, PlaceDto placeRequest) {
        Gardener gardener = gardenerDao.getReferenceById(gardenerId);
        return placeDao.save(placeRequest.toEntityWith(gardener));
    }

    @Override
    @Transactional
    public Place update(PlaceDto placeRequest, Long gardenerId) {
        Place place = placeDao.findByPlaceIdAndGardener_GardenerId(placeRequest.getId(), gardenerId)
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
        placeDao.deleteById(placeId);
    }
}
