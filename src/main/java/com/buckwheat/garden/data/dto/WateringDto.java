package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class WateringDto {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @ToString
    public static class Request {
        private Long id;
        private Long plantId;
        private Long chemicalId;
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
    public static class Response {
        private Long id;
        private String plantName;
        private String chemicalName;
        private LocalDate wateringDate;
        private Message msg;

        public static String getChemicalName(Chemical chemical) {
            if (chemical != null) {
                return chemical.getName();
            }

            return "맹물";
        }

        public static Response from(Watering watering) {
            return Response.builder()
                    .id(watering.getWateringId())
                    .plantName(watering.getPlant().getName())
                    .chemicalName(getChemicalName(watering.getChemical()))
                    .wateringDate(watering.getWateringDate())
                    .build();
        }

        // TODO test 메소드
        public static Response from(LocalDate latestWateringDate){
            return Response.builder().wateringDate(latestWateringDate).build();
        }

        public static Response withWateringMsgFrom(Watering watering, Message wateringMsg) {
            return Response.builder()
                    .id(watering.getWateringId())
                    .plantName(watering.getPlant().getName())
                    .chemicalName(getChemicalName(watering.getChemical()))
                    .wateringDate(watering.getWateringDate())
                    .msg(wateringMsg)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class AfterWatering {
        private PlantDto.Response plant;
        private List<ForOnePlant> waterings;
        private Message wateringMsg;

        public static AfterWatering from(Message wateringMsg, List<ForOnePlant> waterings) {
            return WateringDto.AfterWatering.builder()
                    .wateringMsg(wateringMsg)
                    .waterings(waterings)
                    .build();
        }

        public static AfterWatering from(PlantDto.Response plant, Message wateringMsg, List<ForOnePlant> waterings) {
            return AfterWatering.builder()
                    .plant(plant)
                    .wateringMsg(wateringMsg)
                    .waterings(waterings)
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class Message {
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
    public static class ForOnePlant {
        private Long id;
        private Long chemicalId;
        private String chemicalName;
        private LocalDate wateringDate;
        private int period;

        public static ForOnePlant from(Watering watering) {
            Chemical chemical = watering.getChemical();

            return ForOnePlant.builder()
                    .id(watering.getWateringId())
                    .chemicalId(chemical == null ? 0 : chemical.getChemicalId())
                    .chemicalName(chemical == null ? "맹물" : chemical.getName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }

        public static ForOnePlant withWateringPeriodFrom(Watering watering, int wateringPeriod) {
            Chemical chemical = watering.getChemical();

            return ForOnePlant.builder()
                    .id(watering.getWateringId())
                    .chemicalName(chemical == null ? "맹물" : chemical.getName())
                    .wateringDate(watering.getWateringDate())
                    .period(wateringPeriod)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @ToString
    public static class ByDate {
        private Long id;
        private Long plantId;
        private String plantName;
        private Long chemicalId;
        private String chemicalName;
        private LocalDate wateringDate;

        public static ByDate from(Watering watering) {
            return ByDate.from(watering, watering.getPlant(), watering.getChemical());
        }

        public static ByDate from(Watering watering, Plant plant, Chemical chemical) {
            if (chemical == null) {
                return ByDate.builder()
                        .id(watering.getWateringId())
                        .plantId(plant.getPlantId())
                        .plantName(plant.getName())
                        .wateringDate(watering.getWateringDate())
                        .build();
            }

            return ByDate.builder()
                    .id(watering.getWateringId())
                    .plantId(plant.getPlantId())
                    .plantName(plant.getName())
                    .chemicalId(chemical.getChemicalId())
                    .chemicalName(chemical.getName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class WateringList {
        private LocalDate wateringDate;
        List<ByDate> waterings;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class ResponseInChemical {
        private Long id;
        private Long plantId;
        private String plantName;
        private Long placeId;
        private String placeName;
        private LocalDate wateringDate;

        public static ResponseInChemical from(Watering watering) {
            return ResponseInChemical.builder()
                    .id(watering.getWateringId())
                    .plantId(watering.getPlant().getPlantId())
                    .plantName(watering.getPlant().getName())
                    .placeId(watering.getPlant().getPlace().getPlaceId())
                    .placeName(watering.getPlant().getPlace().getName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }
    }
}
