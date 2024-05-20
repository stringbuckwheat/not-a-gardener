package xyz.notagardener.watering.watering.service;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.watering.Watering;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

public class InvalidWateringRequestProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        Gardener requester = Gardener.builder().gardenerId(3L).build(); // 요청자
        Gardener owner = Gardener.builder().gardenerId(4L).build(); // 실제 소유자

        return Stream.of(
                // 내 약품 X, 내 식물 O
                Arguments.of(
                        Optional.of(Chemical.builder().chemicalId(2L).gardener(owner).build()), // 내 약품이 아님
                        Optional.of(Plant.builder().plantId(1L).gardener(requester).waterings(new ArrayList<Watering>()).build()), // 내 식물 맞음
                        UnauthorizedAccessException.class
                ),
                // 내 약품 O, 내 식물 X
                Arguments.of(
                        Optional.of(Chemical.builder().chemicalId(2L).gardener(requester).build()),
                        Optional.of(Plant.builder().plantId(1L).gardener(owner).waterings(new ArrayList<Watering>()).build()),
                        UnauthorizedAccessException.class
                ),
                // 그런 약품 없음
                Arguments.of(
                        Optional.empty(),
                        Optional.of(Plant.builder().plantId(1L).gardener(owner).waterings(new ArrayList<Watering>()).build()),
                        NoSuchElementException.class
                ),
                // 그런 식물 없음
                Arguments.of(
                        Optional.of(Chemical.builder().chemicalId(2L).gardener(requester).build()),
                        Optional.empty(),
                        NoSuchElementException.class
                )
        );
    }
}
