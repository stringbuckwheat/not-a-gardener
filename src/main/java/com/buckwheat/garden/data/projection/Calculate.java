package com.buckwheat.garden.data.projection;

import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Plant;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Calculate {
    private LocalDate postponeDate;
    private LocalDate birthday;
    private LocalDate latestWateringDate;
    private PlantDto.Response plant;
    private Long gardenerId;

    public static Calculate from(RawGarden rawGarden, Long gardenerId){
        return Calculate.builder()
                .postponeDate(rawGarden.getPostponeDate())
                .birthday(rawGarden.getBirthday())
                .latestWateringDate(rawGarden.getLatestWateringDate())
                .plant(PlantDto.Response.from(rawGarden))
                .gardenerId(gardenerId)
                .build();
    }

    public static Calculate from(Plant plant, Long gardenerId){
        LocalDate latestWateringDate = plant.getWaterings().size() > 0 ? plant.getWaterings().get(0).getWateringDate() : null;

        return Calculate.builder()
                .postponeDate(plant.getPostponeDate())
                .birthday(plant.getBirthday())
                .latestWateringDate(latestWateringDate)
                .plant(PlantDto.Response.from(plant))
                .gardenerId(gardenerId)
                .build();
    }
}
