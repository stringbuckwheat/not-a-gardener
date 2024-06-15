package xyz.notagardener.plant.garden.service;

import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;

import java.time.LocalDate;

public class PlantResponseFactory {
    private PlantStatusResponse status = PlantStatusResponse.builder().attention(YesOrNoType.Y).heavyDrinker(YesOrNoType.N).build();

    public PlantResponse getPostponedPlant() {

        return PlantResponse.builder()
                .plantId(2L)
                .postponeDate(LocalDate.now())
                .createDate(LocalDate.now())
                .status(status)
                .build();
    }

    public PlantResponse getPlantWithNoWateringRecord() {
        return PlantResponse.builder()
                .plantId(3L)
                .postponeDate(null)
                .createDate(LocalDate.now())
                .status(status)
                .build();
    }

    public PlantResponse getWateredTodayPlant() {
        return PlantResponse.builder()
                .plantId(4L)
                .postponeDate(null)
                .recentWateringPeriod(3)
                .latestWateringDate(LocalDate.now())
                .status(status)
                .createDate(LocalDate.now())
                .build();
    }

    public PlantResponse getPlantWithOneWateringRecord() {
        return PlantResponse.builder()
                .plantId(5L)
                .postponeDate(null)
                .recentWateringPeriod(0)
                .latestWateringDate(LocalDate.now().minusDays(1))
                .status(status)
                .createDate(LocalDate.now())
                .build();
    }

    public PlantResponse getThirstyPlant(int wateringPeriod) {
        return PlantResponse.builder()
                .plantId(6L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod))
                .status(status)
                .createDate(LocalDate.now())
                .build();
    }

    public PlantResponse getCheckingPlant(int wateringPeriod) {
        return PlantResponse.builder()
                .plantId(7L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod - 1))
                .createDate(LocalDate.now())
                .status(status)
                .build();
    }

    public PlantResponse getLeaveHerAlonePlant(int wateringPeriod) {
        return PlantResponse.builder()
                .plantId(8L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod - 5))
                .createDate(LocalDate.now())
                .status(status)
                .build();
    }

    public PlantResponse getDryOutPlant(int wateringPeriod, int missedDay) {
        return PlantResponse.builder()
                .plantId(9L)
                .postponeDate(null)
                .recentWateringPeriod(wateringPeriod)
                .latestWateringDate(LocalDate.now().minusDays(wateringPeriod + missedDay))
                .createDate(LocalDate.now())
                .status(status)
                .build();
    }
}
