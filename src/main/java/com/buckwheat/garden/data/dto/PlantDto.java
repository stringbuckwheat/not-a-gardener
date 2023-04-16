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
    public static class Response{
        private int plantNo;
        private String plantName;
        private String plantSpecies;
        private int averageWateringPeriod;
        private int earlyWateringPeriod;
        private String medium;
        private int placeNo;
        private String placeName;
        private LocalDate createDate;
        private LocalDate birthday;

        public static Response from(Plant plant){

            return Response.builder()
                    .plantNo(plant.getPlantNo())
                    .plantName(plant.getPlantName())
                    .plantSpecies(plant.getPlantSpecies())
                    .averageWateringPeriod(plant.getAverageWateringPeriod())
                    .earlyWateringPeriod(plant.getEarlyWateringPeriod())
                    .medium(plant.getMedium())
                    .placeNo(plant.getPlace().getPlaceNo())
                    .placeName(plant.getPlace().getPlaceName())
                    .createDate(LocalDate.from(plant.getCreateDate()))
                    .birthday(plant.getBirthday())
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor
    @ToString
    public static class Request{
        private int plantNo;
        private int placeNo;
        private String plantName;
        private String medium;
        private String plantSpecies;
        private int averageWateringPeriod;
        private LocalDate birthday;

        public Plant toEntityWith(Member member, Place place){
            return Plant.builder()
                    .member(member)
                    .place(place)
                    .plantName(plantName)
                    .medium(medium)
                    .plantSpecies(plantSpecies)
                    .averageWateringPeriod(averageWateringPeriod)
                    .createDate(LocalDateTime.now())
                    .birthday(birthday)
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
