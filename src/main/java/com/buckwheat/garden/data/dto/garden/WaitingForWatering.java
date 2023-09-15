package com.buckwheat.garden.data.dto.garden;

import com.buckwheat.garden.data.entity.Plant;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class WaitingForWatering {
    private Long id;
    private String name;
    private String species;
    private String medium;
    private Long placeId;
    private String placeName;
    private LocalDate createDate;

    public static WaitingForWatering from(Plant plant) {
        return WaitingForWatering.builder()
                .id(plant.getPlantId())
                .name(plant.getName())
                .species(plant.getSpecies())
                .medium(plant.getMedium())
                .placeId(plant.getPlace().getPlaceId())
                .placeName(plant.getPlace().getName())
                .createDate(LocalDate.from(plant.getCreateDate()))
                .build();
    }
}
