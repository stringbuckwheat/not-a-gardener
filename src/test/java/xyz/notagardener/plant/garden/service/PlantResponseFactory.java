package xyz.notagardener.plant.garden.service;

import xyz.notagardener.plant.garden.dto.PlantResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantResponseFactory {
    public PlantResponse getPostponedPlant() {
        return PlantResponse.builder()
                .plantId(2L)
                .postponeDate(LocalDate.now())
                .createDate(LocalDateTime.now())
                .build();
    }

    public PlantResponse getPlantWithNoWateringRecord() {
        return PlantResponse.builder()
                .plantId(3L)
                .postponeDate(null)
                .createDate(LocalDateTime.now())
                .build();
    }

    public PlantResponse getWateredTodayPlant() {
        return PlantResponse.builder()
                .plantId(4L)
                .postponeDate(null)
                .recentWateringPeriod(3)
                .latestWateringDate(LocalDate.now())
                .createDate(LocalDateTime.now())
                .build();
    }

    public PlantResponse getPlantWithOneWateringRecord() {
        return PlantResponse.builder()
                .plantId(5L)
                .postponeDate(null)
                .recentWateringPeriod(0)
                .latestWateringDate(LocalDate.now().minusDays(1))
                .createDate(LocalDateTime.now())
                .build();
    }

    public PlantResponse getThirstyPlant(int wateringPeriod) {
        return PlantResponse.builder()
                .plantId(6L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod))
                .createDate(LocalDateTime.now())
                .build();
    }

    public PlantResponse getCheckingPlant(int wateringPeriod) {
        return PlantResponse.builder()
                .plantId(7L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod - 1))
                .createDate(LocalDateTime.now())
                .build();
    }

    public PlantResponse getLeaveHerAlonePlant(int wateringPeriod) {
        return PlantResponse.builder()
                .plantId(8L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod - 5))
                .createDate(LocalDateTime.now())
                .build();
    }

    public PlantResponse getDryOutPlant(int wateringPeriod, int missedDay) {
        return PlantResponse.builder()
                .plantId(9L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod + missedDay))
                .createDate(LocalDateTime.now())
                .build();
    }
}
