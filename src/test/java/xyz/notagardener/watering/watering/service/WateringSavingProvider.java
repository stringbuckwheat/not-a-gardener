package xyz.notagardener.watering.watering.service;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.dto.PlantStatusType;
import xyz.notagardener.watering.Watering;
import xyz.notagardener.watering.watering.AfterWateringCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class WateringSavingProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        Gardener gardener = Gardener.builder().gardenerId(1L).build();
        Plant plant = Plant.builder().plantId(2L).gardener(gardener).build();
        Plant plantWithPeriod = Plant.builder().plantId(2L).gardener(gardener).recentWateringPeriod(3).build();

        return Stream.of(
                Arguments.of(
                        createWateringList(), // NO_RECORD
                        Optional.empty(), // status
                        AfterWateringCode.NO_RECORD.getCode()
                ),
                Arguments.of(
                        createWateringList(plant), // FIRST_WATERING
                        Optional.empty(),
                        AfterWateringCode.FIRST_WATERING.getCode()
                ),
                Arguments.of(
                        createWateringList(plant, plant), // SECOND_WATERING
                        Optional.empty(), // status
                        AfterWateringCode.SECOND_WATERING.getCode()
                ),
                Arguments.of(
                        createWateringList(plant, plant, plant), // INIT_WATERING_PERIOD
                        Optional.empty(), // 헤비드링커 X
                        AfterWateringCode.POSSIBLE_HEAVY_DRINKER.getCode()
                ),
                Arguments.of(
                        createWateringList(plant, plant, plant), // INIT_WATERING_PERIOD
                        // 이미 헤비 드링커 설정
                        Optional.of(PlantStatusResponse.builder().status(PlantStatusType.HEAVY_DRINKER).active(YesOrNoType.Y).build()),
                        AfterWateringCode.INIT_WATERING_PERIOD.getCode()
                ),
                Arguments.of(
                        List.of( // SCHEDULE_SHORTEN
                                Watering.builder().wateringId(4L).wateringDate(LocalDate.now().minusDays(1)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(3L).wateringDate(LocalDate.now().minusDays(3)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(6)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(9)).plant(plantWithPeriod).build()
                        ),
                        Optional.empty(),
                        AfterWateringCode.SCHEDULE_SHORTEN.getCode()
                ),
                Arguments.of(
                        createWateringList(plantWithPeriod, plantWithPeriod, plantWithPeriod, plantWithPeriod), // NO_CHANGE
                        Optional.empty(),
                        AfterWateringCode.NO_CHANGE.getCode()
                ),
                Arguments.of(
                        List.of( // SCHEDULE_LENGTHEN
                                Watering.builder().wateringId(4L).wateringDate(LocalDate.now().plusDays(1)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(3L).wateringDate(LocalDate.now().minusDays(3)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(6)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(9)).plant(plantWithPeriod).build()
                        ),
                        Optional.empty(),
                        AfterWateringCode.SCHEDULE_LENGTHEN.getCode()
                )
        );
    }

    private static List<Watering> createWateringList(Plant... plants) {
        List<Watering> waterings = new ArrayList<>();

        /**
         * Watering.builder().wateringId(3L).wateringDate(LocalDate.now()).plant(plant).build(), 0 3 6
         * Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(3)).plant(plant).build(),
         * Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(6)).plant(plant).build()
         */

        for (int i = 0; i < plants.length; i++) {
            System.out.println("plusDay: " + 3L * i);
            Watering watering = Watering.builder().wateringId((long) (i + 1))
                    .wateringDate(LocalDate.now().minusDays(3L * i))
                    .plant(plants[i])
                    .build();

            waterings.add(watering);
        }

        System.out.println("--------");

        return waterings;
    }
}
