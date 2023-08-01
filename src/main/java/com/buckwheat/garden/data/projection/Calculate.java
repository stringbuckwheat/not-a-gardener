package com.buckwheat.garden.data.projection;

import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Plant;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Slf4j
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
        log.debug("plant.getWaterings(): {}", plant.getWaterings());
        LocalDate latestWateringDate = null;

        if(plant.getWaterings() != null && plant.getWaterings().size() > 0 ){
            // latestWateringDate = plant.getWaterings().size() > 0 ? plant.getWaterings().get(0).getWateringDate() : null;
            latestWateringDate = plant.getWaterings().get(0).getWateringDate();
        }

        return Calculate.builder()
                .postponeDate(plant.getPostponeDate())
                .birthday(plant.getBirthday())
                .latestWateringDate(latestWateringDate)
                .plant(PlantDto.Response.from(plant))
                .gardenerId(gardenerId)
                .build();
    }
}
