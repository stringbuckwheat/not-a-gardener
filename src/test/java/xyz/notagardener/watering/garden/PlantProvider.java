package xyz.notagardener.watering.garden;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.watering.Watering;
import xyz.notagardener.watering.watering.AfterWateringCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PlantProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        Long plantId = 1L;
        Long gardenerId = 2L;

        int prevPeriod = 5;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant.PlantBuilder base = Plant.builder().plantId(plantId).conditionDate(LocalDate.now().minusDays(10)).gardener(gardener);
        List<Watering> waterings = List.of(
                Watering.builder().wateringId(3L).wateringDate(LocalDate.now().minusDays(prevPeriod)).build(),
                Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(prevPeriod * 2)).build(),
                Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(prevPeriod * 3)).build()
        );

        List<Watering> waterings2 = List.of(
                Watering.builder().wateringId(3L).wateringDate(LocalDate.now().minusDays(prevPeriod - 1)).build(),
                Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(prevPeriod * 2 - 1)).build(),
                Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(prevPeriod * 3 - 1)).build()
        );

        // Plant plant, int expectedAfterWateringCode, int expectedWateringPeriod
        return Stream.of(
                Arguments.of(
                        base.waterings(new ArrayList<>()).recentWateringPeriod(0).build(),
                        AfterWateringCode.NO_RECORD.getCode(),
                        0
                ),
                Arguments.of(
                        base.waterings(waterings2).recentWateringPeriod(prevPeriod).recentWateringPeriod(prevPeriod).build(),
                        AfterWateringCode.NO_CHANGE.getCode(),
                        prevPeriod
                ),
                Arguments.of(
                        base.waterings(waterings).recentWateringPeriod(prevPeriod).recentWateringPeriod(prevPeriod).build(),
                        AfterWateringCode.SCHEDULE_LENGTHEN.getCode(),
                        prevPeriod + 1
                )
        );
    }
}
