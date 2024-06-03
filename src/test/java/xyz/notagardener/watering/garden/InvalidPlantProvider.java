package xyz.notagardener.watering.garden;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.plant.Plant;

import java.util.Optional;
import java.util.stream.Stream;

public class InvalidPlantProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        Long ownerId = 2L;

        Gardener owner = Gardener.builder().gardenerId(ownerId).build();

        Plant plant = Plant.builder().plantId(3L).gardener(owner).build();

        return Stream.of(
                Arguments.of(Optional.empty(), ResourceNotFoundException.class),
                Arguments.of(Optional.of(plant), UnauthorizedAccessException.class)
        );
    }
}
