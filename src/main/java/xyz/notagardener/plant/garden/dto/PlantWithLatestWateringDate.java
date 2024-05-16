package xyz.notagardener.plant.garden.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import xyz.notagardener.plant.Plant;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString(of = {"plantId", "name", "latestWateringDate", "totalWaterings"})
public class PlantWithLatestWateringDate implements RawGarden {
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

    @QueryProjection
    @Builder
    public PlantWithLatestWateringDate(
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
            LocalDate latestWateringDate) {
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
    }

    public PlantWithLatestWateringDate(Plant plant) {
        this.plantId = plant.getPlantId();
        this.name = plant.getName();
        this.species = plant.getSpecies();
        this.recentWateringPeriod = plant.getRecentWateringPeriod();
        this.earlyWateringPeriod = plant.getEarlyWateringPeriod();
        this.medium = plant.getMedium();
        this.birthday = plant.getBirthday();
        this.conditionDate = plant.getConditionDate();
        this.postponeDate = plant.getPostponeDate();
        this.createDate = plant.getCreateDate();
        this.placeId = plant.getPlace().getPlaceId();
        this.placeName = plant.getPlace().getName();

        if(plant.getWaterings().size() > 0) {
            this.wateringId = plant.getWaterings().get(0).getWateringId();
            this.latestWateringDate = plant.getWaterings().get(0).getWateringDate();
        }
    }
}

