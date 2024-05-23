package xyz.notagardener.plant.plant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import xyz.notagardener.common.validation.MediumTypeConstraints;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.place.Place;
import xyz.notagardener.plant.Plant;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
@AllArgsConstructor
public class PlantRequest {
    private Long id;
    @NotBlank(message = "식물 이름은 비워둘 수 없어요")
    private String name;

    @NotBlank(message = "식재 환경은 비워둘 수 없어요")
    @MediumTypeConstraints
    private String medium;

    private String species;

    private int recentWateringPeriod;
    private LocalDate birthday;

    @NotNull(message = "장소는 비워둘 수 없어요")
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
