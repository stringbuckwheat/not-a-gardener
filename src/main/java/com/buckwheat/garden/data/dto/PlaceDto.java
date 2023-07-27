package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PlaceDto {

    /**
     * Place INSERT, UPDATE 요청 시에 사용할 DTO
     */
    @Getter
    @NoArgsConstructor
    @ToString
    @Builder
    @AllArgsConstructor
    public static class Basic{
        private Long id;
        private String name;
        private String artificialLight;
        private String option;

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

        public static Basic from(Place place){
            return Basic.builder()
                    .id(place.getPlaceId())
                    .name(place.getName())
                    .artificialLight(place.getArtificialLight())
                    .option(place.getOption())
                    .build();
        }
    }

    /**
     * 장소 리스트 카드에서 사용할 정보들
     */
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Card{
        private Long id;
        private String name;
        private String artificialLight;
        private String option;
        private int plantListSize;
        private LocalDateTime createDate;

        public static Card from(Place place){
            int plantListSize = place.getPlants() != null ? place.getPlants().size() : 0;

            return Card.builder()
                    .id(place.getPlaceId())
                    .name(place.getName())
                    .artificialLight(place.getArtificialLight())
                    .option(place.getOption())
                    .plantListSize(plantListSize)
                    .createDate(place.getCreateDate())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class WithPlants{
        private Basic place;
        private List<PlantDto.PlantInPlace> plantList;
    }

    @Getter
    @Setter
    @ToString
    public static class ModifyPlace {
        Long placeId;
        List<Long> plantList;
    }
}
