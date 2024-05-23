package xyz.notagardener.plant.garden.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.watering.Watering;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString(of = {"plantId", "name", "latestWateringDate", "totalWatering"})
@EqualsAndHashCode
public class PlantResponse {
    private Long id;
    private String name;
    private String species;
    private int recentWateringPeriod;
    private int earlyWateringPeriod;
    private String medium;
    private LocalDate birthday;
    private LocalDate conditionDate;
    private LocalDate postponeDate;
    private LocalDate createDate;
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
        this.id = plantId;
        this.name = name;
        this.species = species;
        this.recentWateringPeriod = recentWateringPeriod;
        this.earlyWateringPeriod = earlyWateringPeriod;
        this.medium = medium;
        this.birthday = birthday;
        this.conditionDate = conditionDate;
        this.postponeDate = postponeDate;
        this.createDate = LocalDate.from(createDate);
        this.placeId = placeId;
        this.placeName = placeName;
        this.wateringId = wateringId;
        this.latestWateringDate = latestWateringDate;
        this.totalWatering = totalWatering;
    }

    // 첫 번째 생성자
    public PlantResponse(Plant plant) {
        this.id = plant.getPlantId();
        this.name = plant.getName();
        this.species = plant.getSpecies();
        this.recentWateringPeriod = plant.getRecentWateringPeriod();
        this.earlyWateringPeriod = plant.getEarlyWateringPeriod();
        this.medium = plant.getMedium();
        this.placeId = plant.getPlace().getPlaceId();
        this.placeName = plant.getPlace().getName();
        this.createDate = LocalDate.from(plant.getCreateDate());
        this.birthday = plant.getBirthday();
        this.postponeDate = plant.getPostponeDate();
        this.conditionDate = plant.getConditionDate();

        List<Watering> waterings = plant.getWaterings();

        if (waterings != null && !waterings.isEmpty()) {
            Watering latestWatering = waterings.get(0);
            this.wateringId = latestWatering.getWateringId();
            this.latestWateringDate = latestWatering.getWateringDate();
            this.totalWatering = (long) waterings.size();
        }
    }

    // 두 번째 생성자
    public PlantResponse(Plant plant, Long totalWatering, LocalDate latestWateringDate) {
        this.id = plant.getPlantId();
        this.name = plant.getName();
        this.species = plant.getSpecies();
        this.recentWateringPeriod = plant.getRecentWateringPeriod();
        this.earlyWateringPeriod = plant.getEarlyWateringPeriod();
        this.medium = plant.getMedium();
        this.placeId = plant.getPlace().getPlaceId();
        this.placeName = plant.getPlace().getName();
        this.createDate = LocalDate.from(plant.getCreateDate());
        this.birthday = plant.getBirthday();
        this.postponeDate = plant.getPostponeDate();
        this.conditionDate = plant.getConditionDate();

        this.totalWatering = totalWatering;
        this.latestWateringDate = latestWateringDate;
    }
}

