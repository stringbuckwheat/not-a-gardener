package com.buckwheat.garden.data.dto.place;

import com.buckwheat.garden.data.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 장소 리스트 카드에서 사용할 정보들
 */
@Getter
@AllArgsConstructor
@Builder
public class PlaceCard{
    private Long id;
    private String name;
    private String artificialLight;
    private String option;
    private int plantListSize;
    private LocalDateTime createDate;

    public static PlaceCard from(Place place){
        int plantListSize = place.getPlants() != null ? place.getPlants().size() : 0;

        return PlaceCard.builder()
                .id(place.getPlaceId())
                .name(place.getName())
                .artificialLight(place.getArtificialLight())
                .option(place.getOption())
                .plantListSize(plantListSize)
                .createDate(place.getCreateDate())
                .build();
    }
}
