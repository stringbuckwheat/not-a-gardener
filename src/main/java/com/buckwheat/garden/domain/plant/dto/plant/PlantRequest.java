package com.buckwheat.garden.domain.plant.dto.plant;

import com.buckwheat.garden.domain.gardener.Gardener;
import com.buckwheat.garden.domain.place.Place;
import com.buckwheat.garden.domain.plant.Plant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class PlantRequest {
    private Long id;
    private String name;
    private String medium;
    private String species;
    private int recentWateringPeriod;
    private LocalDate birthday;
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
