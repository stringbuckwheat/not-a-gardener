package xyz.notagardener.plant.plant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import xyz.notagardener.common.validation.MediumTypeConstraints;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.place.model.Place;
import xyz.notagardener.plant.model.Plant;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
@AllArgsConstructor
public class PlantRequest {
    @Schema(description = "식물 id", example = "6")
    private Long id;

    @Schema(description = "식물 이름", example = "엄마 온시디움")
    @NotBlank(message = "식물 이름은 비워둘 수 없어요")
    private String name;

    @Schema(description = "식재 환경", example = "수태")
    @NotBlank(message = "식재 환경은 비워둘 수 없어요")
    @MediumTypeConstraints
    private String medium;

    @Schema(description = "식물 종", example = "온시디움 트윙클")
    private String species;

    @Schema(description = "최근 관수 주기", example = "7")
    private int recentWateringPeriod;

    @Schema(description = "키운 날짜", example = "2020-01-03")
    private LocalDate birthday;

    @NotNull(message = "장소는 비워둘 수 없어요")
    @Schema(description = "식물 id", example = "6")
    private Long placeId;

    public Plant toEntityWith(Gardener gardener, Place place) {
        return Plant.builder()
                .gardener(gardener)
                .place(place)
                .name(name)
                .medium(medium)
                .species(species)
                .recentWateringPeriod(recentWateringPeriod)
                .createDate(LocalDateTime.now())
                .birthday(birthday)
                .build();
    }
}
