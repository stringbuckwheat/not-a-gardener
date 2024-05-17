package xyz.notagardener.plant.garden.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.notagardener.plant.Plant;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString(of = {"plantId", "name", "latestWateringDate", "totalWatering"})
@EqualsAndHashCode
public class PlantResponse implements RawGarden {
    private Long plantId;
    private String name;
    private String species;
    private int recentWateringPeriod;
    private int earlyWateringPeriod;
    private String medium;
    private LocalDate birthday;
    private LocalDate conditionDate;
    private LocalDate postponeDate;
    private LocalDateTime createDate;
    private Long placeId;
    private String placeName;
    private Long wateringId;
    private LocalDate latestWateringDate;
    private Long totalWatering;

    @QueryProjection
    @Builder
    public PlantResponse(
            Long plantId,
            String name,
            String species,
            int recentWateringPeriod,
            int earlyWateringPeriod,
            String medium,
            LocalDate birthday,
            LocalDate conditionDate,
            LocalDate postponeDate,
            LocalDateTime createDate,
            Long placeId,
            String placeName,
            Long wateringId,
            LocalDate latestWateringDate,
            Long totalWatering
    ) {
        this.plantId = plantId;
        this.name = name;
        this.species = species;
        this.recentWateringPeriod = recentWateringPeriod;
        this.earlyWateringPeriod = earlyWateringPeriod;
        this.medium = medium;
        this.birthday = birthday;
        this.conditionDate = conditionDate;
        this.postponeDate = postponeDate;
        this.createDate = createDate;
        this.placeId = placeId;
        this.placeName = placeName;
        this.wateringId = wateringId;
        this.latestWateringDate = latestWateringDate;
        this.totalWatering = totalWatering;
    }

    public static PlantResponse from(Plant plant) {
        return PlantResponse.builder()
                .plantId(plant.getPlantId())
                .name(plant.getName())
                .species(plant.getSpecies())
                .recentWateringPeriod(plant.getRecentWateringPeriod())
                .earlyWateringPeriod(plant.getEarlyWateringPeriod())
                .medium(plant.getMedium())
                .placeId(plant.getPlace().getPlaceId())
                .placeName(plant.getPlace().getName())
                .createDate(plant.getCreateDate())
                .birthday(plant.getBirthday())
                .postponeDate(plant.getPostponeDate())
                .conditionDate(plant.getConditionDate())
                .wateringId(plant.getWaterings().size() > 0 ? plant.getWaterings().get(0).getWateringId() : null)
                .latestWateringDate(plant.getWaterings().size() > 0 ? plant.getWaterings().get(0).getWateringDate() : null)
                .totalWatering((long) plant.getWaterings().size())
                .build();
    }

    public static PlantResponse from(Plant plant, Long totalWatering, LocalDate latestWateringDate) {
        return PlantResponse.builder()
                .plantId(plant.getPlantId())
                .name(plant.getName())
                .species(plant.getSpecies())
                .recentWateringPeriod(plant.getRecentWateringPeriod())
                .earlyWateringPeriod(plant.getEarlyWateringPeriod())
                .medium(plant.getMedium())
                .placeId(plant.getPlace().getPlaceId())
                .placeName(plant.getPlace().getName())
                .createDate(plant.getCreateDate())
                .birthday(plant.getBirthday())
                .postponeDate(plant.getPostponeDate())
                .conditionDate(plant.getConditionDate())
                .totalWatering(totalWatering)
                .latestWateringDate(latestWateringDate)
                .build();
    }
}

