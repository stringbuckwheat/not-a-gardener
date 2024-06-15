package xyz.notagardener.watering.watering.service;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.status.common.model.Status;
import xyz.notagardener.watering.Watering;
import xyz.notagardener.watering.watering.AfterWateringCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WateringSavingProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        Gardener gardener = Gardener.builder().gardenerId(1L).build();

        Status heavyDrinker = Status.builder().attention(YesOrNoType.Y).heavyDrinker(YesOrNoType.Y).build();
        Status notHeavyDrinker = Status.builder().attention(YesOrNoType.Y).heavyDrinker(YesOrNoType.N).build();

        Plant plant = Plant.builder().plantId(2L).status(notHeavyDrinker).gardener(gardener).build();
        Plant heavyDrinkingPlant = Plant.builder().plantId(2L).status(heavyDrinker).gardener(gardener).build();
        Plant plantWithPeriod = Plant.builder().plantId(2L).gardener(gardener).status(notHeavyDrinker).recentWateringPeriod(3).build();

        return Stream.of(
                Arguments.of(
                        createWateringList(), // NO_RECORD
                        notHeavyDrinker,
                        AfterWateringCode.NO_RECORD.getCode()
                ),
                Arguments.of(
                        createWateringList(plant), // FIRST_WATERING
                        notHeavyDrinker,
                        AfterWateringCode.FIRST_WATERING.getCode()
                ),
                Arguments.of(
                        createWateringList(plant, plant), // SECOND_WATERING
                        notHeavyDrinker,
                        AfterWateringCode.SECOND_WATERING.getCode()
                ),
                Arguments.of(
                        createWateringList(plant, plant, plant), // INIT_WATERING_PERIOD
                        notHeavyDrinker,
                        AfterWateringCode.POSSIBLE_HEAVY_DRINKER.getCode()
                ),
                Arguments.of(
                        createWateringList(heavyDrinkingPlant, heavyDrinkingPlant, heavyDrinkingPlant), // INIT_WATERING_PERIOD
                        // 이미 헤비 드링커 설정
                        heavyDrinker,
                        AfterWateringCode.INIT_WATERING_PERIOD.getCode()
                ),
                Arguments.of(
                        List.of( // SCHEDULE_SHORTEN
                                Watering.builder().wateringId(4L).wateringDate(LocalDate.now().minusDays(1)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(3L).wateringDate(LocalDate.now().minusDays(3)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(6)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(9)).plant(plantWithPeriod).build()
                        ),
                        notHeavyDrinker,
                        AfterWateringCode.SCHEDULE_SHORTEN.getCode()
                ),
                Arguments.of(
                        createWateringList(plantWithPeriod, plantWithPeriod, plantWithPeriod, plantWithPeriod), // NO_CHANGE
                        notHeavyDrinker,
                        AfterWateringCode.NO_CHANGE.getCode()
                ),
                Arguments.of(
                        List.of( // SCHEDULE_LENGTHEN
                                Watering.builder().wateringId(4L).wateringDate(LocalDate.now().plusDays(1)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(3L).wateringDate(LocalDate.now().minusDays(3)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(6)).plant(plantWithPeriod).build(),
                                Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(9)).plant(plantWithPeriod).build()
                        ),
                        notHeavyDrinker,
                        AfterWateringCode.SCHEDULE_LENGTHEN.getCode()
                )
        );
    }

    private static List<Watering> createWateringList(Plant... plants) {
        List<Watering> waterings = new ArrayList<>();

        for (int i = 0; i < plants.length; i++) {
            Watering watering = Watering.builder().wateringId((long) (i + 1))
                    .wateringDate(LocalDate.now().minusDays(3L * i))
                    .plant(plants[i])
                    .build();

            waterings.add(watering);
        }

        return waterings;
    }
}
