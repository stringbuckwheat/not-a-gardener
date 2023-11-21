package com.buckwheat.garden.place;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.gardener.GardenerRepository;
import com.buckwheat.garden.place.dto.PlaceCard;
import com.buckwheat.garden.place.dto.PlaceDto;
import com.buckwheat.garden.plant.plant.PlantInPlace;
import com.buckwheat.garden.data.entity.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlaceCard> getAll(Long gardenerId) {
        return placeRepository.findByGardener_GardenerIdOrderByCreateDate(gardenerId)
                .stream()
                .map(PlaceCard::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceDto getDetail(Long placeId, Long gardenerId) {
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(placeId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
        Long plantListSize = placeRepository.countPlantsByPlaceId(placeId);
        return PlaceDto.from(place, plantListSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantInPlace> getPlantsWithPaging(Long placeId, Pageable pageable) {
        return placeRepository.findPlantsByPlaceIdWithPage(placeId, pageable).stream()
                .map(PlantInPlace::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlaceCard add(Long gardenerId, PlaceDto placeRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        Place place =  placeRepository.save(placeRequest.toEntityWith(gardener));

        return PlaceCard.from(place);
    }

    @Override
    @Transactional
    public PlaceDto update(PlaceDto placeRequest, Long gardenerId) {
        Place place = placeRepository.findByPlaceIdAndGardener_GardenerId(placeRequest.getId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        place.update(
                placeRequest.getName(),
                placeRequest.getOption(),
                placeRequest.getArtificialLight()
        );

        return PlaceDto.from(place);
    }

    @Override
    public void delete(Long placeId, Long gardenerId) {
        placeRepository.deleteById(placeId);
    }
}
