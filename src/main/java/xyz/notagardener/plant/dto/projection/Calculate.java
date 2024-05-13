package xyz.notagardener.domain.plant.dto.projection;

import xyz.notagardener.domain.plant.Plant;
import xyz.notagardener.domain.plant.dto.plant.PlantResponse;
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
    private PlantResponse plant;
    private Long gardenerId;

    public static Calculate from(RawGarden rawGarden, Long gardenerId) {
        return Calculate.builder()
                .postponeDate(rawGarden.getPostponeDate())
                .birthday(rawGarden.getBirthday())
                .latestWateringDate(rawGarden.getLatestWateringDate())
                .plant(PlantResponse.from(rawGarden))
                .gardenerId(gardenerId)
                .build();
    }

    public static Calculate from(Plant plant, Long gardenerId) {
        LocalDate latestWateringDate = null;

        if (plant.getWaterings() != null && plant.getWaterings().size() > 0) {
            latestWateringDate = plant.getWaterings().get(0).getWateringDate();
        }

        return Calculate.builder()
                .postponeDate(plant.getPostponeDate())
                .birthday(plant.getBirthday())
                .latestWateringDate(latestWateringDate)
                .plant(PlantResponse.from(plant))
                .gardenerId(gardenerId)
                .build();
    }
}
