package com.buckwheat.garden.plant.plant;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
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
