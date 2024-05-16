package xyz.notagardener.plant.garden.service;

import xyz.notagardener.plant.garden.dto.PlantWithLatestWateringDate;
import xyz.notagardener.plant.garden.dto.RawGarden;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RawGardenFactory {
    public RawGarden getPostponedPlant() {
        return PlantWithLatestWateringDate.builder()
                .plantId(2L)
                .postponeDate(LocalDate.now())
                .createDate(LocalDateTime.now())
                .build();
    }

    public RawGarden getPlantWithNoWateringRecord() {
        return PlantWithLatestWateringDate.builder()
                .plantId(3L)
                .postponeDate(null)
                .createDate(LocalDateTime.now())
                .build();
    }

    public RawGarden getWateredTodayPlant() {
        return PlantWithLatestWateringDate.builder()
                .plantId(4L)
                .postponeDate(null)
                .recentWateringPeriod(3)
                .latestWateringDate(LocalDate.now())
                .createDate(LocalDateTime.now())
                .build();
    }

    public RawGarden getPlantWithOneWateringRecord() {
        return PlantWithLatestWateringDate.builder()
                .plantId(5L)
                .postponeDate(null)
                .recentWateringPeriod(0)
                .latestWateringDate(LocalDate.now().minusDays(1))
                .createDate(LocalDateTime.now())
                .build();
    }

    public RawGarden getThirstyPlant(int wateringPeriod) {
        return PlantWithLatestWateringDate.builder()
                .plantId(6L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod))
                .createDate(LocalDateTime.now())
                .build();
    }

    public RawGarden getCheckingPlant(int wateringPeriod) {
        return PlantWithLatestWateringDate.builder()
                .plantId(7L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod - 1))
                .createDate(LocalDateTime.now())
                .build();
    }

    public RawGarden getLeaveHerAlonePlant(int wateringPeriod) {
        return PlantWithLatestWateringDate.builder()
                .plantId(8L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod - 5))
                .createDate(LocalDateTime.now())
                .build();
    }

    public RawGarden getDryOutPlant(int wateringPeriod, int missedDay) {
        return PlantWithLatestWateringDate.builder()
                .plantId(9L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod + missedDay))
                .createDate(LocalDateTime.now())
                .build();
    }
}
