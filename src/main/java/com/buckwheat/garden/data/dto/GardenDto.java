package com.buckwheat.garden.data.dto;

import lombok.*;

import java.util.List;


public class GardenDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class GardenResponse {
        private PlantDto.PlantResponse plant;
        private GardenDetail gardenDetail;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class GardenMain {
        private List<GardenResponse> todoList;
        private List<GardenDto.WaitingForWatering> waitingList;
        private List<RoutineDto.Response> routineList;
    }

    @AllArgsConstructor
    @Getter
    public static class WaitingForWatering{
        private int plantNo;
        private String plantName;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class GardenDetail {
        // 마지막 관수
        private WateringDto.WateringResponse latestWateringDate;

        // 이하 계산해서 넣는 정보
        private String anniversary; // 키운지 며칠 지났는지
        private int wateringDDay;

        // 물주기 정보
        private int wateringCode;

        // 비료 주기 정보
        private int chemicalCode;

        public static GardenDetail from(WateringDto.WateringResponse latestWateringDate, String anniversary, int wateringDDay, int wateringCode, int chemicalCode){
            return GardenDetail.builder()
                    .latestWateringDate(latestWateringDate)
                    .anniversary(anniversary)
                    .wateringDDay(wateringDDay)
                    .wateringCode(wateringCode)
                    .chemicalCode(chemicalCode)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class WateringResponse {
        private GardenResponse gardenResponse;
        private WateringDto.WateringMsg wateringMsg;
    }
}
