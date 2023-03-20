package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import lombok.*;

import java.time.LocalDate;


public class GardenDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class GardenResponse {
        private PlantDto.PlantResponse plant;
        private GardenDetail gardenDetail;
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
        private int fertilizingCode;

        public static GardenDetail from(WateringDto.WateringResponse latestWateringDate, String anniversary, int wateringDDay, int wateringCode, int fertilizingCode){
            return GardenDetail.builder()
                    .latestWateringDate(latestWateringDate)
                    .anniversary(anniversary)
                    .wateringDDay(wateringDDay)
                    .wateringCode(wateringCode)
                    .fertilizingCode(fertilizingCode)
                    .build();
        }
    }
}
