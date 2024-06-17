package xyz.notagardener.plant.garden.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
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
    @Schema(description = "식물 id", example = "9")
    private Long id;

    @Schema(description = "식물 이름", example = "보보")
    private String name;

    @Schema(description = "식물 종", example = "샌더소니 블루")
    private String species;

    @Schema(description = "최근 관수 간격", example = "2")
    private int recentWateringPeriod;

    @Schema(description = "초기 관수 간격", example = "5")
    private int earlyWateringPeriod;

    @Schema(description = "식재 환경", example = "흙과 화분")
    private String medium;

    @Schema(description = "키운 날짜", example = "2024-04-01")
    private LocalDate birthday;

    @Schema(description = "(가장 최근)물 안 마른 날짜", example = "2024-05-01")
    private LocalDate conditionDate;

    @Schema(description = "(가장 최근)물 주기 미룬 날짜", example = "2024-05-23")
    private LocalDate postponeDate;

    @Schema(description = "데이터 생성일", example = "2024-04-02")
    private LocalDate createDate;

    @Schema(description = "장소 id", example = "99")
    private Long placeId;

    @Schema(description = "장소 이름", example = "창가")
    private String placeName;

    @Schema(description = "가장 최근 물 준 기록 id", example = "23")
    private Long wateringId;

    @Schema(description = "가장 최근 물 준 기록 id", example = "2024-06-15")
    private LocalDate latestWateringDate;

    @Schema(description = "총 물 준 횟수", example = "20")
    private Long totalWatering;

    @Schema(description = "현재 식물 상태")
    private PlantStatusResponse status;

    @QueryProjection
    public PlantResponse(Plant plant, Place place, Long wateringId, LocalDate latestWateringDate, Long totalWatering, Status status) {
        if (plant == null) {
            throw new ResourceNotFoundException(ExceptionCode.ACCESS_NOT_ALLOWED);
        }

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

