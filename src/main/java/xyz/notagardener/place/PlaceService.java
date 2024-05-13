package xyz.notagardener.domain.place;

import xyz.notagardener.domain.place.dto.PlaceCard;
import xyz.notagardener.domain.place.dto.PlaceDto;
import xyz.notagardener.domain.plant.dto.plant.PlantInPlace;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceService {
    List<PlaceCard> getAll(Long gardenerId);

    PlaceDto getDetail(Long placeId, Long gardenerId);

    List<PlantInPlace> getPlantsWithPaging(Long placeId, Pageable pageable);

    PlaceCard add(Long gardenerId, PlaceDto placeRequest);

    PlaceDto update(PlaceDto placeRequest, Long gardenerId);

    void delete(Long placeId, Long gardenerId);
}