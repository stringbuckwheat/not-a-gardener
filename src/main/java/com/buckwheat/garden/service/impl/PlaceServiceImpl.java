package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    @Override
    @Transactional(readOnly = true) // LazyInitializationException
    public List<PlaceDto.PlaceCard> getPlaceList(int memberNo) {
        List<PlaceDto.PlaceCard> list = new ArrayList<>();

        for(Place p : placeRepository.findByMember_memberNo(memberNo)){
            list.add(
                    PlaceDto.PlaceCard.builder()
                            .placeNo(p.getPlaceNo())
                            .placeName(p.getPlaceName())
                            .artificialLight(p.getArtificialLight())
                            .option(p.getOption())
                            .plantListSize(p.getPlantList().size())
                            .build()
            );
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceDto getPlace(int placeNo) {
        Place place = placeRepository.findById(placeNo).orElseThrow(NoSuchElementException::new);
        List<Plant> list = place.getPlantList();
        log.debug("list: {}", list);

        List<PlantDto> plantDtoList = new ArrayList<>();

        for(Plant pl : place.getPlantList()){
            PlantDto plantDto = PlantDto.builder()
                    .plantNo(pl.getPlantNo())
                    .plantName(pl.getPlantName())
                    .plantSpecies(pl.getPlantSpecies())
                    .averageWateringPeriod(pl.getAverageWateringPeriod())
                    .medium(pl.getMedium())
                    .createDate(LocalDate.from(pl.getCreateDate()))
                    .build();

            plantDtoList.add(plantDto);
        }

        return PlaceDto.builder()
                .placeNo(place.getPlaceNo())
                .placeName(place.getPlaceName())
                .artificialLight(place.getArtificialLight())
                .option(place.getOption())
                // .plantList(plantDtoList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantDto.PlantInPlace> getPlantlistInPlace(int placeNo) {
        Place place = placeRepository.findById(placeNo).orElseThrow(NoSuchElementException::new);

        List<PlantDto.PlantInPlace> plantList = new ArrayList<>();

        for(Plant pl : place.getPlantList()){
            plantList.add(
                    PlantDto.PlantInPlace.builder()
                            .plantNo(pl.getPlantNo())
                            .plantName(pl.getPlantName())
                            .plantSpecies(pl.getPlantSpecies())
                            .averageWateringPeriod(pl.getAverageWateringPeriod())
                            .medium(pl.getMedium())
                            .createDate(LocalDate.from(pl.getCreateDate()))
                            .build()
            );
        }

        return plantList;
    }

    @Override
    public PlaceDto addPlace(PlaceDto placeDto, Member member) {
        Place place = placeRepository.save(placeDto.toEntityWithMember(member));

        return PlaceDto.builder()
                .placeNo(place.getPlaceNo())
                .placeName(place.getPlaceName())
                .artificialLight(place.getArtificialLight())
                .option(place.getOption())
                .build();
    }

    @Override
    public void modifyPlace(PlaceDto placeDto, Member member) {
        Place place = placeRepository.findById(placeDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        Place updatedPlace = place.update(placeDto.getPlaceName(), placeDto.getOption(), placeDto.getArtificialLight());
        placeRepository.save(updatedPlace);
    }

    @Override
    public void deletePlace(int placeNo) {
        placeRepository.deleteById(placeNo);
    }
}
