package xyz.notagardener.plant.garden.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import xyz.notagardener.place.Place;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;
import xyz.notagardener.status.common.model.Status;
import xyz.notagardener.watering.Watering;

import java.time.LocalDate;
import java.util.List;

@Getter
@EqualsAndHashCode(of = {"id", "name", "latestWateringDate", "totalWatering"})
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
    private PlantStatusResponse status;

    @QueryProjection
    public PlantResponse(Plant plant, Place place, Long wateringId, LocalDate latestWateringDate, Long totalWatering, Status status) {
        this.id = plant.getPlantId();
        this.name = plant.getName();
        this.species = plant.getSpecies();
        this.recentWateringPeriod = plant.getRecentWateringPeriod();
        this.earlyWateringPeriod = plant.getEarlyWateringPeriod();
        this.medium = plant.getMedium();
        this.birthday = plant.getBirthday();
        this.conditionDate = plant.getConditionDate();
        this.postponeDate = plant.getPostponeDate();
        this.createDate = LocalDate.from(plant.getCreateDate());
        this.placeId = place.getPlaceId();
        this.placeName = place.getName();
        this.wateringId = wateringId;
        this.latestWateringDate = latestWateringDate;
        this.totalWatering = totalWatering;
        this.status = new PlantStatusResponse(status);
    }
    public PlantResponse(Plant plant, Long totalWatering, LocalDate latestWateringDate, PlantStatusResponse status) {
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
        this.status = status;
    }

     // Without Status
    public PlantResponse(Plant plant) {
        this(plant, null, null, null);

        List<Watering> waterings = plant.getWaterings();

        if (waterings != null && !waterings.isEmpty()) {
            Watering latestWatering = waterings.get(0);

            this.wateringId = latestWatering.getWateringId();
            this.latestWateringDate = latestWatering.getWateringDate();
            this.totalWatering = (long) waterings.size();
        }

        this.status = new PlantStatusResponse(plant.getStatus());
    }

    public PlantResponse(Plant plant, PlantStatusResponse status) {
        this(plant);
        this.status = status;
    }

    // 테스트코드 용
    @Builder
    public PlantResponse(Long plantId, int recentWateringPeriod, LocalDate latestWateringDate, LocalDate postponeDate, LocalDate createDate, PlantStatusResponse status) {
        this.id = plantId;
        this.recentWateringPeriod = recentWateringPeriod;
        this.postponeDate = postponeDate;
        this.latestWateringDate = latestWateringDate;
        this.createDate = createDate;
        this.status = status;
    }
}

