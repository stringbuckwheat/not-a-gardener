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
    public static class Response {
        private PlantDto.Response plant;
        private Detail gardenDetail;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class GardenMain {
        private boolean hasPlant;
        private List<Response> todoList;
        private List<GardenDto.WaitingForWatering> waitingList;
        private List<RoutineDto.Response> routineList;
    }

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Getter
    @ToString
    public static class WaitingForWatering{
        private Long id;
        private String name;
        private String species;
        private String medium;
        private Long placeId;
        private String placeName;
        private LocalDate createDate;

        public static WaitingForWatering from(Plant plant){
            return WaitingForWatering.builder()
                    .id(plant.getPlantId())
                    .name(plant.getName())
                    .species(plant.getSpecies())
                    .medium(plant.getMedium())
                    .placeId(plant.getPlace().getPlaceId())
                    .placeName(plant.getPlace().getName())
                    .createDate(LocalDate.from(plant.getCreateDate()))
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class Detail {
        // 마지막 관수
        private WateringDto.Response latestWateringDate;

        // 이하 계산해서 넣는 정보
        private String anniversary; // 키운지 며칠 지났는지
        private int wateringDDay;

        // 물주기 정보
        private int wateringCode;

        // 비료 주기 정보
        ChemicalCode chemicalCode;
    }

    @AllArgsConstructor
    @Getter
    public static class ChemicalCode{
        private Long chemicalId;
        private String chemicalName;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class WateringResponse {
        private Response gardenResponse;
        private WateringDto.Message wateringMsg;
    }
}
