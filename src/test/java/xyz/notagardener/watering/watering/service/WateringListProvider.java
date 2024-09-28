package xyz.notagardener.watering.watering.service;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.watering.model.Watering;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WateringListProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        LocalDate date = LocalDate.of(2024, 4, 19);

        // 매일 같은 식물
        List<Watering> waterings = new ArrayList<>(42);
        Plant plant = Plant.builder().plantId(1L).build();

        for (long i = 1; i <= 42; i++) {
            waterings.add(Watering.builder().wateringId(i).wateringDate(date).plant(plant).build());
            date = date.plusDays(1);
        }

        // 물 준 날짜 2개, 두 날짜 마다 여러개의 식물
        Plant plant2 = Plant.builder().plantId(2L).build();
        Plant plant3 = Plant.builder().plantId(3L).build();

        List<Watering> waterings2 = List.of(
                Watering.builder().wateringId(1L).wateringDate(date).plant(plant).build(),
                Watering.builder().wateringId(2L).wateringDate(date).plant(plant2).build(),
                Watering.builder().wateringId(3L).wateringDate(date).plant(plant3).build(),
                Watering.builder().wateringId(4L).wateringDate(date.plusDays(1)).plant(plant).build(),
                Watering.builder().wateringId(5L).wateringDate(date.plusDays(1)).plant(plant3).build(),
                Watering.builder().wateringId(6L).wateringDate(date.plusDays(1)).plant(plant3).build()
        );


        return Stream.of(
                Arguments.of(waterings, 42),
                Arguments.of(waterings2, 2),
                Arguments.of(new ArrayList<Watering>(), 0)
        );
    }
}
