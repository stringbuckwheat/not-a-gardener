package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class WateringDto {

    @NoArgsConstructor
    @Getter
    @ToString
    public static class WateringRequest {
        private int wateringNo;
        private int plantNo;
        private int chemicalNo;
        private LocalDate wateringDate;

        public Watering toEntityWithPlantAndChemical(Plant plant, Chemical chemical) {
            return Watering.builder()
                    .plant(plant)
                    .chemical(chemical)
                    .wateringDate(wateringDate)
                    .build();
        }

        public Watering toEntityWithPlant(Plant plant) {
            return Watering.builder()
                    .plant(plant)
                    .wateringDate(wateringDate)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class WateringResponse {
        private int wateringNo;
        private String plantName;
        private String chemicalName;
        private LocalDate wateringDate;
        private WateringMsg wateringMsg;

        public static String getChemicalName(Chemical chemical) {
            if (chemical != null) {
                return chemical.getChemicalName();
            }

            return "맹물";
        }

        public static WateringResponse from(Watering watering) {
            return WateringResponse.builder()
                    .wateringNo(watering.getWateringNo())
                    .plantName(watering.getPlant().getPlantName())
                    .chemicalName(getChemicalName(watering.getChemical()))
                    .wateringDate(watering.getWateringDate())
                    .build();
        }

        public static WateringResponse withWateringMsgFrom(Watering watering, WateringMsg wateringMsg) {
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
    @Builder
    @Getter
    @ToString
    public static class WateringModifyResponse {
        private PlantDto.PlantResponse plant;
        private List<WateringForOnePlant> wateringList;
        private WateringMsg wateringMsg;
    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class WateringMsg {
        // -1   물주기가 줄어들었어요!
        // 0    물주기 계산에 변동이 없습니다.
        // 1    물주기가 늘어났어요
        // 2    처음으로 물주기를 기록
        // 3    두 번째 물주기 기록

        private int afterWateringCode;
        private int averageWateringDate; // 며칠만에 물 줬는지
    }

    @AllArgsConstructor
    @Builder
    @Getter // (InvalidDefinitionException - No serializer found for class)
    @ToString
    public static class WateringForOnePlant {
        private int wateringNo;
        private int chemicalNo;
        private String chemicalName;
        private LocalDate wateringDate;
        private int wateringPeriod;

        public static WateringForOnePlant from(Watering watering) {
            Chemical chemical = watering.getChemical();

            return WateringForOnePlant.builder()
                    .wateringNo(watering.getWateringNo())
                    .chemicalNo(chemical == null ? 0 : chemical.getChemicalNo())
                    .chemicalName(chemical == null ? "맹물" : chemical.getChemicalName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }

        public static WateringForOnePlant withWateringPeriodFrom(Watering watering, int wateringPeriod) {
            Chemical chemical = watering.getChemical();

            return WateringForOnePlant.builder()
                    .wateringNo(watering.getWateringNo())
                    .chemicalName(chemical == null ? "맹물" : chemical.getChemicalName())
                    .wateringDate(watering.getWateringDate())
                    .wateringPeriod(wateringPeriod)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Getter
    @ToString
    public static class ByDate{
        private int wateringNo;
        private int plantNo;
        private String plantName;
        private int chemicalNo;
        private String chemicalName;
        private LocalDate wateringDate;

        public static ByDate from(Watering watering) {

            if (watering.getChemical() == null) {
                return ByDate.builder()
                        .wateringNo(watering.getWateringNo())
                        .plantNo(watering.getPlant().getPlantNo())
                        .plantName(watering.getPlant().getPlantName())
                        .wateringDate(watering.getWateringDate())
                        .build();
            }

            return ByDate.builder()
                    .wateringNo(watering.getWateringNo())
                    .plantNo(watering.getPlant().getPlantNo())
                    .plantName(watering.getPlant().getPlantName())
                    .chemicalNo(watering.getChemical().getChemicalNo())
                    .chemicalName(watering.getChemical().getChemicalName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class WateringList {
        private LocalDate wateringDate;
        List<ByDate> wateringList;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class WateringResponseInChemical {
        private int wateringNo;
        private int plantNo;
        private String plantName;
        private int placeNo;
        private String placeName;
        private LocalDate wateringDate;

        public static WateringResponseInChemical from(Watering watering) {
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
