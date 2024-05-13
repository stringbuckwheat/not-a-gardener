package xyz.notagardener.domain.plant.dto.plant;

import xyz.notagardener.domain.plant.Plant;
import xyz.notagardener.domain.plant.dto.projection.RawGarden;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "식물 id", example = "1")
    private Long id;

    @Schema(description = "식물 이름", example = "아수라 백작")
    private String name;

    @Schema(description = "식물 종", example = "오키나와 실버")
    private String species;

    @Schema(description = "최근 물 준 간격", example = "5")
    private int recentWateringPeriod;

    @Schema(description = "최초 물 준 간격", example = "7")
    private int earlyWateringPeriod;

    @Schema(description = "식재 환경", example = "흙과 화분")
    private String medium;

    @Schema(description = "장소 id", example = "4")
    private Long placeId;

    @Schema(description = "장소 이름", example = "책상 위")
    private String placeName;

    @Schema(description = "작성일", example = "2022-10-02")
    private LocalDate createDate;

    @Schema(description = "작성일", example = "2018-07-30")
    private LocalDate birthday;

    @Schema(description = "(가장 최근) 물주기를 미룬 날짜", example = "2023-01-30")
    private LocalDate postponeDate;

    @Schema(description = "(가장 최근) 물이 안 마른 날짜", example = "2023-01-11")
    private LocalDate conditionDate;

    @Schema(description = "총 물 준 횟수", example = "99")
    private Long totalWaterings;

    @Schema(description = "(가장 최근) 물 준 날", example = "2023-01-28")
    private LocalDate latestWateringDate;

    @Schema(description = "물 주기 id", example = "99")
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

    public static PlantResponse from(Plant plant, Long totalWaterings, LocalDate latestWateringDate) {
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