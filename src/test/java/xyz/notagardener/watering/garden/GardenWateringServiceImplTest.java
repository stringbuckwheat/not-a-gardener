package xyz.notagardener.watering.garden;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.place.Place;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.garden.dto.GardenDetail;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.garden.service.GardenResponseMapperImpl;
import xyz.notagardener.plant.garden.service.WateringCode;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.watering.Watering;
import xyz.notagardener.watering.garden.dto.GardenWateringResponse;
import xyz.notagardener.watering.garden.service.GardenWateringServiceImpl;
import xyz.notagardener.watering.watering.AfterWateringCode;
import xyz.notagardener.watering.watering.dto.AfterWatering;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.watering.service.WateringCommandService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("메인 페이지 물주기 컴포넌트 테스트")
class GardenWateringServiceImplTest {
    @Mock
    private WateringCommandService wateringCommandService;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private GardenResponseMapperImpl gardenResponseMapper;

    @InjectMocks
    private GardenWateringServiceImpl gardenWateringService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("메인 페이지 물주기 추가: 성공")
    void add_ShouldReturnGardenWateringResponse() {
        // Given
        Long gardenerId = 1L;

        Long plantId = 2L;
        WateringRequest request = WateringRequest.builder().plantId(plantId).wateringDate(LocalDate.now()).build();

        Watering watering = Watering.builder().wateringId(1L).wateringDate(request.getWateringDate()).build();
        Place place = Place.builder().placeId(3L).build();

        Plant plant = Plant.builder().plantId(plantId).place(place).waterings(List.of(watering)).createDate(LocalDateTime.now()).build();
        WateringMessage wateringMessage = new WateringMessage(AfterWateringCode.FIRST_WATERING.getCode(), 0);

        AfterWatering afterWatering = new AfterWatering(plant, wateringMessage);
        GardenResponse gardenResponse = new GardenResponse(new PlantResponse(plant), GardenDetail.notEnoughRecord(null), false);

        when(wateringCommandService.add(request, gardenerId)).thenReturn(afterWatering);
        when(gardenResponseMapper.getGardenResponse(new PlantResponse(plant), gardenerId)).thenReturn(gardenResponse);

        // When
        GardenWateringResponse result = gardenWateringService.add(gardenerId, request);

        // Then
        assertNotNull(result);
    }

    @ParameterizedTest
    @ArgumentsSource(PlantProvider.class)
    @DisplayName("안 말랐어요: 성공")
    void notDry_WhenPlantExistAndMine_ShouldReturnWateringMessage(Plant plant, String expectedAfterWateringCode, int expectedWateringPeriod) {
        // Given
        Long plantId = 1L;
        Long gardenerId = 2L;

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When
        WateringMessage result = gardenWateringService.notDry(plantId, gardenerId);

        // Then
        System.out.println("***" + result.getRecentWateringPeriod());

        assertNotNull(result);
        assertEquals(LocalDate.now(), plant.getConditionDate());
        assertEquals(expectedAfterWateringCode, result.getAfterWateringCode());
        assertEquals(expectedWateringPeriod, result.getRecentWateringPeriod());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPlantProvider.class)
    @DisplayName("안 말랐어요: 그런 식물 없거나 내 식물 아님 - 실패")
    void notDry_WhenPlantNotExistOrNotMine_ShouldThrowException(Optional<Plant> plant, Class<RuntimeException> expectedType) {
        // Given
        Long plantId = 3L;
        Long requesterId = 1L;
        when(plantRepository.findByPlantId(plantId)).thenReturn(plant);

        // When, Then
        assertThrows(expectedType, () -> gardenWateringService.notDry(plantId, requesterId));
    }

    @Test
    @DisplayName("물 주기 미루기: 성공")
    void postpone_WhenPlantExistAndMine_ShouldReturnWateringCode() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When
        String result = gardenWateringService.postpone(plantId, gardenerId);

        // Then
        assertEquals(WateringCode.YOU_ARE_LAZY.getCode(), result);
        assertEquals(LocalDate.now(), plant.getPostponeDate());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidPlantProvider.class)
    @DisplayName("물 주기 미루기: 그런 식물 없거나 내 식물 아님 - 실패")
    void postpone_WhenPlantNotExistOrNotMine_ShouldThrowException(Optional<Plant> plant, Class<RuntimeException> expectedType) {
        // Given
        Long plantId = 3L;
        Long requesterId = 1L;
        when(plantRepository.findByPlantId(plantId)).thenReturn(plant);

        // When, Then
        assertThrows(expectedType, () -> gardenWateringService.postpone(plantId, requesterId));
    }
}