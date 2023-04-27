package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
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
    public static class Request{
        private Long id;
        private String name;
        private String artificialLight;
        private String option;

        /**
         * createDate로 쓸 LocalDateTime.now()를 포함한 엔티티를 반환
         * @param member FK 매핑
         * @return Place
         */
        public Place toEntityWith(Member member){
            return Place.builder()
                    .placeId(id)
                    .name(name)
                    .artificialLight(artificialLight)
                    .option(option)
                    .createDate(LocalDateTime.now())
                    .member(member)
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
            return Card.builder()
                    .id(place.getPlaceId())
                    .name(place.getName())
                    .artificialLight(place.getArtificialLight())
                    .option(place.getOption())
                    .plantListSize(place.getPlants().size())
                    .createDate(place.getCreateDate())
                    .build();
        }

        public static Card fromNew(Place place){
            return Card.builder()
                    .id(place.getPlaceId())
                    .name(place.getName())
                    .artificialLight(place.getArtificialLight())
                    .option(place.getOption())
                    .plantListSize(0)
                    .createDate(place.getCreateDate())
                    .build();
        }
    }

    /**
     * 장소 CRUD의 Response로 사용할 DTO
     */
    @AllArgsConstructor
    @Builder
    @ToString
    @Getter
    public static class Response{
        private Long id;
        private String name;
        private String artificialLight;
        private String option;

        public static Response from(Place place){
            return Response.builder()
                    .id(place.getPlaceId())
                    .name(place.getName())
                    .artificialLight(place.getArtificialLight())
                    .option(place.getOption())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class WithPlantList{
        private Response place;
        private List<PlantDto.PlantInPlace> plantList;
    }

    @Getter
    @Setter
    @ToString
    public static class ModifyPlantPlace {
        Long placeId;
        List<Long> plantList;
    }
}
