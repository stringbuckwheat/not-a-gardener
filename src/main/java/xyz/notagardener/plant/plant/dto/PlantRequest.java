package xyz.notagardener.plant.plant.dto;

import lombok.*;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.place.Place;
import xyz.notagardener.plant.Plant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
@AllArgsConstructor
public class PlantRequest {
    private Long id;
    @NotBlank
    private String name;

    private String medium;

    private String species;

    private int recentWateringPeriod;
    private LocalDate birthday;

    @NotNull
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
