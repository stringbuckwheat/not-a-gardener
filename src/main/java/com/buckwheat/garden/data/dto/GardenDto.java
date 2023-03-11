package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Plant;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GardenDto {
    // plant의 정보
    private int plantNo;
    private String plantName;
    private String plantSpecies;
    private int averageWateringPeriod;

    // 장소 정보
    private int placeNo;
    private String placeName;

    // 이하 계산해서 넣는 정보
    private String anniversary; // 키운지 며칠 지났는지
    private int wateringDDay;

    // 물주기 정보
    private int wateringCode;

    // 비료 주기 정보
    private int fertilizingCode;

    public static GardenDto from(Plant plant, String anniversary, int wateringDDay, int wateringCode){
        return GardenDto.builder()
                .plantNo(plant.getPlantNo())
                .plantName(plant.getPlantName())
                .plantSpecies(plant.getPlantSpecies())
                .averageWateringPeriod(plant.getAverageWateringPeriod())
                .placeNo(plant.getPlace().getPlaceNo())
                .placeName(plant.getPlace().getPlaceName())
                .anniversary(anniversary)
                .wateringDDay(wateringDDay)
                .wateringCode(wateringCode)
                .fertilizingCode(999999)
                .build();
    }
}
