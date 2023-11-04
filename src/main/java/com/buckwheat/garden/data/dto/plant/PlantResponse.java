package com.buckwheat.garden.data.dto.plant;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.projection.RawGarden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class PlantResponse {
    private Long id;
    private String name;
    private String species;
    private int recentWateringPeriod;
    private int earlyWateringPeriod;
    private String medium;
    private Long placeId;
    private String placeName;
    private LocalDate createDate;
    private LocalDate birthday;
    private LocalDate postponeDate;
    private LocalDate conditionDate;

    private int totalWaterings;
    private LocalDate latestWateringDate;

    private long wateringId;

    public static PlantResponse from(Plant plant) {
        return PlantResponse.builder()
                .id(plant.getPlantId())
                .name(plant.getName())
                .species(plant.getSpecies())
                .recentWateringPeriod(plant.getRecentWateringPeriod())
                .earlyWateringPeriod(plant.getEarlyWateringPeriod())
                .medium(plant.getMedium())
                .placeId(plant.getPlace().getPlaceId())
                .placeName(plant.getPlace().getName())
                .createDate(LocalDate.from(plant.getCreateDate()))
                .birthday(plant.getBirthday())
                .postponeDate(plant.getPostponeDate())
                .conditionDate(plant.getConditionDate())
                .build();
    }

    public static PlantResponse from(Plant plant, int totalWaterings, LocalDate latestWateringDate) {
        return PlantResponse.builder()
                .id(plant.getPlantId())
                .name(plant.getName())
                .species(plant.getSpecies())
                .recentWateringPeriod(plant.getRecentWateringPeriod())
                .earlyWateringPeriod(plant.getEarlyWateringPeriod())
                .medium(plant.getMedium())
                .placeId(plant.getPlace().getPlaceId())
                .placeName(plant.getPlace().getName())
                .createDate(LocalDate.from(plant.getCreateDate()))
                .birthday(plant.getBirthday())
                .postponeDate(plant.getPostponeDate())
                .conditionDate(plant.getConditionDate())
                .totalWaterings(totalWaterings)
                .latestWateringDate(latestWateringDate)
                .build();
    }

    public static PlantResponse from(RawGarden rawGarden) {
        return PlantResponse.builder()
                .id(rawGarden.getPlantId())
                .name(rawGarden.getName())
                .species(rawGarden.getSpecies())
                .recentWateringPeriod(rawGarden.getRecentWateringPeriod())
                .earlyWateringPeriod(rawGarden.getEarlyWateringPeriod())
                .medium(rawGarden.getMedium())
                .placeId(rawGarden.getPlaceId())
                .placeName(rawGarden.getPlaceName())
                .createDate(LocalDate.from(rawGarden.getCreateDate()))
                .birthday(rawGarden.getBirthday())
                .postponeDate(rawGarden.getPostponeDate())
                .conditionDate(rawGarden.getConditionDate())
                .build();
    }
}