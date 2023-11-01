package com.buckwheat.garden.data.dto.place;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import lombok.*;

import java.time.LocalDateTime;


/* Place INSERT, UPDATE 요청 시에 사용할 DTO */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
@AllArgsConstructor
public class PlaceDto{
    private Long id;
    private String name;
    private String artificialLight;
    private String option;
    private int plantListSize;

    /**
     * createDate로 쓸 LocalDateTime.now()를 포함한 엔티티를 반환
     * @param gardener FK 매핑
     * @return Place
     */
    public Place toEntityWith(Gardener gardener){
        return Place.builder()
                .placeId(id)
                .name(name)
                .artificialLight(artificialLight)
                .option(option)
                .createDate(LocalDateTime.now())
                .gardener(gardener)
                .build();
    }

    public static PlaceDto from(Place place){
        return PlaceDto.builder()
                .id(place.getPlaceId())
                .name(place.getName())
                .artificialLight(place.getArtificialLight())
                .option(place.getOption())
                .build();
    }

    public static PlaceDto from(Place place, int plantListSize){
        return PlaceDto.builder()
                .id(place.getPlaceId())
                .name(place.getName())
                .artificialLight(place.getArtificialLight())
                .option(place.getOption())
                .plantListSize(plantListSize)
                .build();
    }
}
