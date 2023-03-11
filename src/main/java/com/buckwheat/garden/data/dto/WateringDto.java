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

    @NoArgsConstructor
    @Getter
    @ToString
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

        public Watering toEntityWithPlant(Plant plant){
            return Watering.builder()
                    .plant(plant)
                    .wateringDate(wateringDate)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class WateringResponse{
        private int wateringNo;
        private String plantName;
        private String fertilizerName;
        private LocalDate wateringDate;

        public static WateringResponse from(Watering watering){
            String fertilizerName = "맹물";

            if(watering.getFertilizer() != null){
                fertilizerName = watering.getFertilizer().getFertilizerName();
            }

            return WateringResponse.builder()
                    .wateringNo(watering.getWateringNo())
                    .plantName(watering.getPlant().getPlantName())
                    .fertilizerName(fertilizerName)
                    .wateringDate(watering.getWateringDate())
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter // (InvalidDefinitionException - No serializer found for class)
    @ToString
    public static class WateringForOnePlant{
        private int wateringNo;
        private String fertilizerName;
        private LocalDate wateringDate;
        private int wateringPeriod;

        private static String getFertilizerName(Fertilizer fertilizer){
            return fertilizer == null ? "맹물" : fertilizer.getFertilizerName();
        }

        public static WateringForOnePlant from(Watering watering){
            return WateringForOnePlant.builder()
                    .wateringNo(watering.getWateringNo())
                    .fertilizerName(getFertilizerName(watering.getFertilizer()))
                    .wateringDate(watering.getWateringDate())
                    .build();
        }

        public static WateringForOnePlant withWateringPeriodFrom(Watering watering, int wateringPeriod){
            return WateringForOnePlant.builder()
                    .wateringNo(watering.getWateringNo())
                    .fertilizerName(getFertilizerName(watering.getFertilizer()))
                    .wateringDate(watering.getWateringDate())
                    .wateringPeriod(wateringPeriod)
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
