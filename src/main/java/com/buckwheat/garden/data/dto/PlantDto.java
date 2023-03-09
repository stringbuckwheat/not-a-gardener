package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantDto {
    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class PlantResponse{
        private int plantNo;
        private String plantName;
        private String plantSpecies;
        private int averageWateringPeriod;
        private String medium;
        private int placeNo;
        private String placeName;
        private LocalDate createDate;

        public static PlantResponse from(Plant plant){
            return PlantResponse.builder()
                    .plantNo(plant.getPlantNo())
                    .plantName(plant.getPlantName())
                    .plantSpecies(plant.getPlantSpecies())
                    .averageWateringPeriod(plant.getAverageWateringPeriod())
                    .placeNo(plant.getPlace().getPlaceNo())
                    .placeName(plant.getPlace().getPlaceName())
                    .medium(plant.getMedium())
                    .createDate(LocalDate.from(plant.getCreateDate()))
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor
    @ToString
    public static class PlantRequest{
        private int plantNo;
        private int placeNo;
        private String plantName;
        private String medium;
        private String plantSpecies;
        private int averageWateringPeriod;

        public Plant toEntityWithMemberAndPlace(Member member, Place place){
            return Plant.builder()
                    .member(member)
                    .place(place)
                    .plantNo(plantNo)
                    .plantName(plantName)
                    .medium(medium)
                    .plantSpecies(plantSpecies)
                    .averageWateringPeriod(averageWateringPeriod)
                    .createDate(LocalDateTime.now())
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class PlantInPlace{
        private int plantNo;
        private String plantName;
        private String plantSpecies;
        private int averageWateringPeriod;
        private String medium;
        private LocalDate createDate;

        public static PlantInPlace from(Plant plant){
            return PlantInPlace.builder()
                    .plantNo(plant.getPlantNo())
                    .plantName(plant.getPlantName())
                    .plantSpecies(plant.getPlantSpecies())
                    .averageWateringPeriod(plant.getAverageWateringPeriod())
                    .medium(plant.getMedium())
                    .createDate(LocalDate.from(plant.getCreateDate()))
                    .build();
        }
    }
}
