package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.place.PlaceCard;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.dto.plant.PlantInPlace;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.repository.command.PlaceCommandRepository;
import com.buckwheat.garden.repository.query.PlaceQueryRepository;
import com.buckwheat.garden.service.PlaceService;
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
    private final PlaceCommandRepository placeCommandRepository;
    private final PlaceQueryRepository placeQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlaceCard> getAll(Long gardenerId) {
        return placeQueryRepository.findByGardener_GardenerIdOrderByCreateDate(gardenerId)
                .stream()
                .map(PlaceCard::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceDto getDetail(Long placeId, Long gardenerId) {
        Place place = placeQueryRepository.findByPlaceIdAndGardener_GardenerId(placeId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
        Long plantListSize = placeQueryRepository.countPlantsByPlaceId(placeId);
        return PlaceDto.from(place, plantListSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantInPlace> getPlantsWithPaging(Long placeId, Pageable pageable) {
        return placeQueryRepository.findPlantsByPlaceIdWithPage(placeId, pageable).stream()
                .map(PlantInPlace::from)
                .collect(Collectors.toList());
    }

    @Override
    public PlaceCard add(Long gardenerId, PlaceDto placeRequest) {
        // FK인 Gardener와 createDate로 쓸 LocalDateTime.now()를 포함한 엔티티를 저장
        Place place = placeCommandRepository.save(gardenerId, placeRequest);
        return PlaceCard.from(place);
    }

    @Override
    public PlaceDto update(PlaceDto placeRequest, Long gardenerId) {
        return PlaceDto.from(placeCommandRepository.update(placeRequest, gardenerId));
    }

    @Override
    public void delete(Long placeId, Long gardenerId) {
        placeCommandRepository.deleteBy(placeId, gardenerId);
    }
}
