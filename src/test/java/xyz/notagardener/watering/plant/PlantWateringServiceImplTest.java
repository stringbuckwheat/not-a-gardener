package xyz.notagardener.watering.plant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import xyz.notagardener.place.Place;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.watering.Watering;
import xyz.notagardener.watering.plant.dto.PlantWateringResponse;
import xyz.notagardener.watering.plant.dto.WateringForOnePlant;
import xyz.notagardener.watering.watering.AfterWateringCode;
import xyz.notagardener.watering.watering.dto.AfterWatering;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.watering.repository.WateringRepository;
import xyz.notagardener.watering.watering.service.WateringCommandService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PlantWateringServiceImplTest {
    @Mock
    private WateringCommandService wateringCommandService;

    @Mock
    private WateringRepository wateringQueryRepository;

    @InjectMocks
    private PlantWateringServiceImpl plantWateringService;

    private List<Watering> waterings;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        int prevPeriod = 4;
        waterings = List.of(
                Watering.builder().wateringId(4L).wateringDate(LocalDate.now()).build(),
                Watering.builder().wateringId(3L).wateringDate(LocalDate.now().minusDays(prevPeriod)).build(),
                Watering.builder().wateringId(2L).wateringDate(LocalDate.now().minusDays(prevPeriod * 2)).build(),
                Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(prevPeriod * 3)).build()
        );
    }

    @Test
    void add_ShouldReturnPlantWateringResponse() {
        // Given
        Long gardenerId = 1L;
        Long plantId = 2L;
        int period = 3;
        WateringRequest request = WateringRequest.builder().plantId(plantId).wateringDate(LocalDate.now()).build();

        Place place = Place.builder().placeId(4L).build();
        Plant plant = Plant.builder().plantId(plantId).recentWateringPeriod(period).place(place).waterings(waterings).build();
        WateringMessage wateringMessage = new WateringMessage(AfterWateringCode.NO_CHANGE.getCode(), period);
        AfterWatering afterWatering = new AfterWatering(plant, wateringMessage);

        Pageable pageable = mock(Pageable.class);

        when(wateringCommandService.add(request, gardenerId)).thenReturn(afterWatering);
        when(wateringQueryRepository.findWateringsByPlantIdWithPage(plantId, pageable)).thenReturn(waterings);

        // When
        PlantWateringResponse result = plantWateringService.add(request, pageable, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(AfterWateringCode.NO_CHANGE.getCode(), result.getWateringMsg().getAfterWateringCode());
        assertEquals(waterings.size(), result.getWaterings().size());
        assertTrue(result.getWaterings().get(0).getPeriod() != 0);
    }

    @Test
    void getAll_WhenWateringSizeMoreThanTwo_ShouldReturnWithPeriodList() {
        // Given
        Long plantId = 1L;
        Pageable pageable = mock(Pageable.class);

        when(wateringQueryRepository.findWateringsByPlantIdWithPage(plantId, pageable)).thenReturn(waterings);

        // When
        List<WateringForOnePlant> result = plantWateringService.getAll(plantId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(waterings.size(), result.size());
        assertEquals(0, result.get(result.size() - 1).getPeriod()); // 마지막 요소는 0

        for (int i = 0; i < result.size() - 1; i++) { // 마지막 요소 빼고
            assertTrue(result.get(i).getPeriod() != 0);
        }
    }

    @Test
    void getAll_WhenWateringsSizeLessThanTwo_ShouldReturnWithoutPeriod() {
        // Given
        Long plantId = 1L;
        Pageable pageable = mock(Pageable.class);

        when(wateringQueryRepository.findWateringsByPlantIdWithPage(plantId, pageable))
                .thenReturn(List.of(Watering.builder().build()));

        // When
        List<WateringForOnePlant> result = plantWateringService.getAll(plantId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter((w) -> w.getPeriod() == 0).count());
    }
}