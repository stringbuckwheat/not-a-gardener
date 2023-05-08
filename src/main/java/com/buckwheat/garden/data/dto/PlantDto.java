package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Gardener;
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
    public static class Response{
        private Long id;
        private String name;
        private String species;
        private int recentWateringPeriod;
        private int earlyWateringPeriod;
        private String medium;
        private Long placeId;
        private String placeName;
        private LocalDate createDate;
        private LocalDate birthday;
        private LocalDate postponeDate;
        private LocalDate conditionDate;

        public static Response from(Plant plant){

            return Response.builder()
                    .id(plant.getPlantId())
                    .name(plant.getName())
                    .species(plant.getSpecies())
                    .recentWateringPeriod(plant.getRecentWateringPeriod())
                    .earlyWateringPeriod(plant.getEarlyWateringPeriod())
                    .medium(plant.getMedium())
                    .placeId(plant.getPlace().getPlaceId())
                    .placeName(plant.getPlace().getName())
                    .createDate(LocalDate.from(plant.getCreateDate()))
                    .birthday(plant.getBirthday())
                    .postponeDate(plant.getPostponeDate())
                    .conditionDate(plant.getConditionDate())
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor
    @ToString
    public static class Request{
        private Long id;
        private String name;
        private String medium;
        private String species;
        private int recentWateringPeriod;
        private LocalDate birthday;
        private Long placeId;

        public Plant toEntityWith(Gardener gardener, Place place){
            return Plant.builder()
                    .gardener(gardener)
                    .place(place)
                    .name(name)
                    .medium(medium)
                    .species(species)
                    .recentWateringPeriod(recentWateringPeriod)
                    .createDate(LocalDateTime.now())
                    .birthday(birthday)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class PlantInPlace{
        private Long id;
        private String name;
        private String species;
        private int recentWateringPeriod;
        private String medium;
        private LocalDate createDate;

        public static PlantInPlace from(Plant plant){
            return PlantInPlace.builder()
                    .id(plant.getPlantId())
                    .name(plant.getName())
                    .species(plant.getSpecies())
                    .recentWateringPeriod(plant.getRecentWateringPeriod())
                    .medium(plant.getMedium())
                    .createDate(LocalDate.from(plant.getCreateDate()))
                    .build();
        }
    }
}
