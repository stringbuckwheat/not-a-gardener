package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import lombok.*;

import java.time.LocalDate;

public class WateringDto {

    @NoArgsConstructor
    @Getter
    @ToString
    public static class WateringRequest{
        private int plantNo;
        private int chemicalNo;
        private LocalDate wateringDate;

        public Watering toEntityWithPlantAndChemical(Plant plant, Chemical chemical){
            return Watering.builder()
                    .plant(plant)
                    .chemical(chemical)
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
        private String chemicalName;
        private LocalDate wateringDate;
        private WateringMsg wateringMsg;

        public static String getChemicalName(Chemical chemical){
            if(chemical != null){
                return chemical.getChemicalName();
            }

            return "맹물";
        }

        public static WateringResponse from(Watering watering){


            return WateringResponse.builder()
                    .wateringNo(watering.getWateringNo())
                    .plantName(watering.getPlant().getPlantName())
                    .chemicalName(getChemicalName(watering.getChemical()))
                    .wateringDate(watering.getWateringDate())
                    .build();
        }

        public static WateringResponse withWateringMsgFrom(Watering watering, WateringMsg wateringMsg){
            return WateringResponse.builder()
                    .wateringNo(watering.getWateringNo())
                    .plantName(watering.getPlant().getPlantName())
                    .chemicalName(getChemicalName(watering.getChemical()))
                    .wateringDate(watering.getWateringDate())
                    .wateringMsg(wateringMsg)
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    public static class WateringMsg{
        private int wateringCode;
        private int averageWateringDate; // 며칠만에 물 줬는지
    }

    @AllArgsConstructor
    @Builder
    @Getter // (InvalidDefinitionException - No serializer found for class)
    @ToString
    public static class WateringForOnePlant{
        private int wateringNo;
        private String chemicalName;
        private LocalDate wateringDate;
        private int wateringPeriod;

        private static String getChemicalName(Chemical chemical){
            return chemical == null ? "맹물" : chemical.getChemicalName();
        }

        public static WateringForOnePlant from(Watering watering){
            return WateringForOnePlant.builder()
                    .wateringNo(watering.getWateringNo())
                    .chemicalName(getChemicalName(watering.getChemical()))
                    .wateringDate(watering.getWateringDate())
                    .build();
        }

        public static WateringForOnePlant withWateringPeriodFrom(Watering watering, int wateringPeriod){
            return WateringForOnePlant.builder()
                    .wateringNo(watering.getWateringNo())
                    .chemicalName(getChemicalName(watering.getChemical()))
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
        private String chemicalName;
        private LocalDate wateringDate;

        public static WateringList from(Watering watering){
            return WateringList.builder()
                    .wateringNo(watering.getWateringNo())
                    .plantName(watering.getPlant().getPlantName())
                    .chemicalName(watering.getChemical().getChemicalName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class WateringResponseInChemical{
        private int wateringNo;
        private int plantNo;
        private String plantName;
        private int placeNo;
        private String placeName;
        private LocalDate wateringDate;

        public static WateringResponseInChemical from(Watering watering){
            return WateringResponseInChemical.builder()
                    .wateringNo(watering.getWateringNo())
                    .plantNo(watering.getPlant().getPlantNo())
                    .plantName(watering.getPlant().getPlantName())
                    .placeNo(watering.getPlant().getPlace().getPlaceNo())
                    .placeName(watering.getPlant().getPlace().getPlaceName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }
    }
}
