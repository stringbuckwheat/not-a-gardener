package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.code.WateringCode;
import com.buckwheat.garden.data.entity.Plant;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;


public class GardenDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    public static class Response {
        private PlantDto.Response plant;
        private Detail gardenDetail;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    public static class GardenMain {
        private boolean hasPlant;
        private List<Response> todoList;
        private List<GardenDto.WaitingForWatering> waitingList;
        private List<RoutineDto.Response> routineList;
    }

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @ToString
    public static class WaitingForWatering {
        private Long id;
        private String name;
        private String species;
        private String medium;
        private Long placeId;
        private String placeName;
        private LocalDate createDate;

        public static WaitingForWatering from(Plant plant) {
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

        public static Detail lazy(LocalDate latestWateringDate, LocalDate birthday) {
            return GardenDto.Detail.builder()
                    .latestWateringDate(latestWateringDate == null ?
                            null : WateringDto.Response.from(latestWateringDate))
                    .anniversary(getAnniversary(birthday))
                    .wateringDDay(latestWateringDate == null ? -1 : 0)
                    .wateringCode(WateringCode.YOU_ARE_LAZY.getCode())
                    .chemicalCode(null)
                    .build();
        }

        public static Detail noRecord(LocalDate birthday){
            return GardenDto.Detail.builder()
                    .latestWateringDate(null)
                    .anniversary(getAnniversary(birthday))
                    .wateringDDay(-1)
                    .wateringCode(WateringCode.NO_RECORD.getCode())
                    .chemicalCode(null)
                    .build();
        }

        public static String getAnniversary(LocalDate birthday) {
            if (birthday == null) {
                return "";
            }

            LocalDate today = LocalDate.now();

            // 생일이면
            if (today.getMonth() == birthday.getMonth() && today.getDayOfMonth() == birthday.getDayOfMonth()) {
                return "생일 축하해요";
            }

            return Duration.between(birthday.atStartOfDay(), today.atStartOfDay()).toDays() + "일 째 반려중";
        }
    }

    @AllArgsConstructor
    @Getter
    public static class ChemicalCode {
        private Long chemicalId;
        private String chemicalName;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    public static class WateringResponse {
        private Response gardenResponse;
        private WateringDto.Message wateringMsg;
    }
}
