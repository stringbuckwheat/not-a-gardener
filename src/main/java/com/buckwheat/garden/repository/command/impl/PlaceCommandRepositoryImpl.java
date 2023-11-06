package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.repository.command.PlaceCommandRepository;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.repository.GardenerRepository;
import com.buckwheat.garden.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class PlaceCommandRepositoryImpl implements PlaceCommandRepository {
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;

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
