package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Fertilizer;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import lombok.*;

import java.time.LocalDate;

public class WateringDto {
//    private int wateringNo;
//    private int plantNo;
//    private int fertilizerNo;
//    private String fertilizerName;
//    private LocalDate wateringDate;

    @AllArgsConstructor
    @Builder
    @Getter
    public static class WateringRequest{
        private int plantNo;
        private int fertilizerNo;
        private LocalDate wateringDate;

        public Watering toEntityWithPlantAndFertilizer(Plant plant, Fertilizer fertilizer){
            return Watering.builder()
                    .plant(plant)
                    .fertilizer(fertilizer)
                    .wateringDate(wateringDate)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    public static class WateringResponse{
        private int wateringNo;
        private String plantName;
        private String fertilizerName;
        private LocalDate wateringDate;

        public static WateringResponse from(Watering watering){
            return WateringResponse.builder()
                    .wateringNo(watering.getWateringNo())
                    .plantName(watering.getPlant().getPlantName())
                    .fertilizerName(watering.getFertilizer().getFertilizerName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    public static class WateringList{
        private int wateringNo;
        private String plantName;
        private String fertilizerName;
        private LocalDate wateringDate;

        public static WateringList from(Watering watering){
            return WateringList.builder()
                    .wateringNo(watering.getWateringNo())
                    .plantName(watering.getPlant().getPlantName())
                    .fertilizerName(watering.getFertilizer().getFertilizerName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }
    }
}
