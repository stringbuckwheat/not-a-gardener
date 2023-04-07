package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Plant;
import lombok.*;

import java.time.LocalDate;
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
    @Builder
    @NoArgsConstructor
    @Getter
    public static class WaitingForWatering{
        private int plantNo;
        private String plantName;
        private String plantSpecies;
        private String medium;
        private int placeNo;
        private String placeName;
        private LocalDate createDate;

        public static WaitingForWatering from(Plant plant){
            return WaitingForWatering.builder()
                    .plantNo(plant.getPlantNo())
                    .plantName(plant.getPlantName())
                    .plantSpecies(plant.getPlantSpecies())
                    .medium(plant.getMedium())
                    .placeNo(plant.getPlace().getPlaceNo())
                    .placeName(plant.getPlace().getPlaceName())
                    .createDate(LocalDate.from(plant.getCreateDate()))
                    .build();
        }
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
        ChemicalCode chemicalCode;

        public static GardenDetail from(WateringDto.WateringResponse latestWateringDate, String anniversary, int wateringDDay, int wateringCode, ChemicalCode chemicalCode){
            return GardenDetail.builder()
                    .latestWateringDate(latestWateringDate)
                    .anniversary(anniversary)
                    .wateringDDay(wateringDDay)
                    .wateringCode(wateringCode)
                    .chemicalCode(chemicalCode)
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    public static class ChemicalCode{
        private int chemicalNo;
        private String chemicalName;
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
