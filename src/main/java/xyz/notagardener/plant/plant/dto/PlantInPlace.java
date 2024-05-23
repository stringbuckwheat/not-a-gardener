package xyz.notagardener.plant.plant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import xyz.notagardener.plant.Plant;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class PlantInPlace {
    @Schema(description = "식물 id", example = "6")
    private Long id;

    @Schema(description = "식물 이름", example = "엄마 온시디움")
    private String name;

    @Schema(description = "식물 종", example = "온시디움 트윙클")
    private String species;

    @Schema(description = "최근 관수 주기", example = "7")
    private int recentWateringPeriod;

    @Schema(description = "식재 환경", example = "수태")
    private String medium;

    @Schema(description = "등록일", example = "2024-02-01")
    private LocalDate createDate;

    public PlantInPlace(Plant plant) {
        this.id = plant.getPlantId();
        this.name = plant.getName();
        this.species = plant.getSpecies();
        this.recentWateringPeriod = plant.getRecentWateringPeriod();
        this.medium = plant.getMedium();
        this.createDate = LocalDate.from(plant.getCreateDate());
    }
}