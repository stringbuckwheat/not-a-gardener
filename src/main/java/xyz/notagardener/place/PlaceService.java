package xyz.notagardener.place;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.place.dto.PlaceCard;
import xyz.notagardener.place.dto.PlaceDto;
import xyz.notagardener.plant.plant.dto.PlantInPlace;

import java.util.List;

public interface PlaceService {
    List<PlaceCard> getAll(Long gardenerId);

    PlaceDto getDetail(Long placeId, Long gardenerId);

    List<PlantInPlace> getPlantsWithPaging(Long placeId, Pageable pageable);

    PlaceCard add(Long gardenerId, PlaceDto placeRequest);

    PlaceDto update(PlaceDto placeRequest, Long gardenerId);

    void delete(Long placeId, Long gardenerId);
}
