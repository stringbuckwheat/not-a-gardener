package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import lombok.*;

public class PlaceDto {

    /**
     * Place INSERT, UPDATE 시에 사용할 DTO
     */
    @Getter
    @NoArgsConstructor
    @ToString
    public static class PlaceRequestDto{
        private int placeNo;
        private String placeName;
        private String artificialLight;
        private String option;

        public Place toEntityWithMember(Member member){
            return Place.builder()
                    .placeNo(placeNo)
                    .placeName(placeName)
                    .artificialLight(artificialLight)
                    .option(option)
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
    public static class PlaceCard{
        private int placeNo;
        private String placeName;
        private String artificialLight;
        private String option;
        private int plantListSize;

        public static PlaceCard from(Place place){
            return PlaceDto.PlaceCard.builder()
                    .placeNo(place.getPlaceNo())
                    .placeName(place.getPlaceName())
                    .artificialLight(place.getArtificialLight())
                    .option(place.getOption())
                    .plantListSize(place.getPlantList().size())
                    .build();
        }
    }

    /**
     * 장소 CRUD의 Response로 사용할 DTO
     */
    // @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    @Getter
    public static class PlaceResponseDto{
        private int placeNo;
        private String placeName;
        private String artificialLight;
        private String option;

        /**
         * 도메인 객체에 dto 의존을 추가하지 않고 dto 반환 목적
         * @param place dto의 내용을 채워넣을 place 엔티티
         * @return 클라이언트 응답으로 쓸 DTO
         */
        public static PlaceResponseDto from(Place place){
            return PlaceResponseDto.builder()
                    .placeNo(place.getPlaceNo())
                    .placeName(place.getPlaceName())
                    .artificialLight(place.getArtificialLight())
                    .option(place.getOption())
                    .build();
        }
    }
}
