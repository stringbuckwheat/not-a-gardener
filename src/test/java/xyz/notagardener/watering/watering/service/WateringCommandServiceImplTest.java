package xyz.notagardener.watering.watering.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.chemical.model.Chemical;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.common.error.exception.AlreadyWateredException;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.status.common.model.Status;
import xyz.notagardener.status.common.repository.StatusLogRepository;
import xyz.notagardener.watering.model.Watering;
import xyz.notagardener.watering.watering.AfterWateringCode;
import xyz.notagardener.watering.watering.dto.AfterWatering;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.watering.repository.WateringRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("물주기 저장/수정/삭제 컴포넌트 테스트")
class WateringCommandServiceImplTest {
    @Mock
    private WateringRepository wateringRepository;

    @Mock
    private ChemicalRepository chemicalRepository;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private StatusLogRepository statusLogRepository;

    @InjectMocks
    private WateringCommandServiceImpl wateringCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ArgumentsSource(WateringSavingProvider.class)
    @DisplayName("물주기 기록 추가 (with chemical): 성공")
    void add_WithChemicalId_ShouldReturnWateringAndAfterWateringDto(List<Watering> latestFourWaterings, Status status, String code) {
        // Given
        Long gardenerId = 1L;
        Long plantId = 2L;
        Long chemicalId = 3L;
        LocalDate wateringDate = LocalDate.now();

        WateringRequest request = WateringRequest.builder().plantId(plantId).chemicalId(chemicalId).wateringDate(wateringDate).build();
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Chemical chemical = Chemical.builder().chemicalId(chemicalId).gardener(gardener).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).status(status).waterings(new ArrayList<Watering>()).build();

        when(wateringRepository.existByWateringDateAndPlantId(wateringDate, chemicalId)).thenReturn(false);
        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.of(chemical));
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));
        when(wateringRepository.findLatestFourWateringDate(plantId)).thenReturn(latestFourWaterings);

        // When
        AfterWatering result = wateringCommandService.add(request, gardenerId);

        // Then
        assertEquals(plant, result.getPlant());

        WateringMessage wateringMessage = result.getWateringMessage();

        boolean isBeforeInitPeriod = latestFourWaterings.isEmpty() || latestFourWaterings.size() < 3;

        assertNotNull(wateringMessage);
        assertEquals(code, wateringMessage.getAfterWateringCode());

        if (latestFourWaterings.size() == 3) {
            assertEquals(3, result.getPlant().getRecentWateringPeriod());
            assertEquals(3, result.getPlant().getEarlyWateringPeriod());
        } else if (AfterWateringCode.SCHEDULE_SHORTEN.getCode().equals(code)) {
            assertEquals(2, result.getPlant().getRecentWateringPeriod());
        } else if (AfterWateringCode.NO_CHANGE.getCode().equals(code)) {
            assertEquals(3, result.getPlant().getRecentWateringPeriod());
        } else if (AfterWateringCode.SCHEDULE_LENGTHEN.getCode().equals(code)) {
            assertEquals(4, result.getPlant().getRecentWateringPeriod());
        }
    }

    @Test
    @DisplayName("물주기 기록 추가 (without chemical): 성공")
    void add_WithoutChemicalId_ShouldReturnWateringAndAfterWateringDto() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 3L;
        LocalDate wateringDate = LocalDate.now();

        WateringRequest request = WateringRequest.builder().plantId(plantId).wateringDate(wateringDate).build();
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).waterings(new ArrayList<Watering>()).build();
        Watering watering = Watering.builder().wateringId(1L).wateringDate(LocalDate.now()).plant(plant).build();

        when(wateringRepository.existByWateringDateAndPlantId(wateringDate, plantId)).thenReturn(false);
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        when(wateringRepository.findLatestFourWateringDate(plantId)).thenReturn(List.of(watering));

        // When
        AfterWatering result = wateringCommandService.add(request, gardenerId);

        // Then
        WateringMessage wateringMessage = result.getWateringMessage();

        assertEquals(plant, result.getPlant());
        assertNotNull(wateringMessage);
        assertEquals(AfterWateringCode.FIRST_WATERING.getCode(), wateringMessage.getAfterWateringCode());
    }

    @Test
    @DisplayName("물주기 등록: 물주기 이미 존재 - 실패")
    void add_WhenWateringExistOnTheDay_ShouldThrowAlreadyWateredException() {
        // Given
        Long plantId = 1L;
        Long chemicalId = 2L;
        WateringRequest request = WateringRequest.builder().plantId(plantId).chemicalId(chemicalId).wateringDate(LocalDate.now()).build();

        when(wateringRepository.existByWateringDateAndPlantId(request.getWateringDate(), request.getPlantId()))
                .thenReturn(true);

        // When, Then
        assertThrows(AlreadyWateredException.class, () -> wateringCommandService.add(request, 1L));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidWateringRequestProvider.class)
    @DisplayName("물주기 등록: 내 약품 또는 식물이 아님 - 실패")
    void add_WhenRequestDataInvalid_ShouldThrowUnauthorizedAccessExceptionOrResourceNotFoundException
            (Optional<Chemical> chemical, Optional<Plant> plant, Class<RuntimeException> expectedType) {
        // Given
        Long plantId = 1L;
        Long chemicalId = 2L;
        LocalDate wateringDate = LocalDate.now();

        WateringRequest request = WateringRequest.builder().plantId(plantId).chemicalId(chemicalId).wateringDate(wateringDate).build();

        when(wateringRepository.existByWateringDateAndPlantId(wateringDate, chemicalId)).thenReturn(false);
        when(chemicalRepository.findById(chemicalId)).thenReturn(chemical);
        when(plantRepository.findByPlantId(plantId)).thenReturn(plant);

        // When, Then
        assertThrows(expectedType, () -> wateringCommandService.add(request, 3L));
    }

    @Test
    @DisplayName("물 주기 수정: 성공")
    void updateWateringPeriod_ShouldReturnAfterWatering() {
        // Given
        Long plantId = 1L;
        Long chemicalId = 2L;
        Long gardenerId = 3L;
        Long wateringId = 4L;
        LocalDate wateringDate = LocalDate.now();

        WateringRequest request = WateringRequest.builder().id(wateringId).plantId(plantId).chemicalId(chemicalId).wateringDate(wateringDate).build();
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).waterings(new ArrayList<Watering>()).build();
        Chemical chemical = Chemical.builder().chemicalId(chemicalId).gardener(gardener).build();
        Watering watering = Watering.builder().wateringId(wateringId).wateringDate(LocalDate.now().minusDays(3)).plant(plant).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));
        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.of(chemical));
        when(wateringRepository.findByWateringIdAndPlant_PlantId(wateringId, plantId)).thenReturn(Optional.of(watering));
        when(wateringRepository.findLatestFourWateringDate(plantId)).thenReturn(List.of(watering));

        // When
        AfterWatering result = wateringCommandService.update(request, gardenerId);

        // Then
        WateringMessage wateringMessage = result.getWateringMessage();

        assertEquals(plant, result.getPlant());
        assertNotNull(wateringMessage);
        assertEquals(AfterWateringCode.FIRST_WATERING.getCode(), wateringMessage.getAfterWateringCode());
    }

    @Test
    @DisplayName("물주기 수정: 그런 물주기 기록 없음 - 실패")
    void update_WhenWateringNotExistOrNotMine_ShouldThrowResourceNotFoundException() {
        // Given
        Long plantId = 1L;
        Long chemicalId = 2L;
        Long gardenerId = 3L;
        Long wateringId = 4L;
        LocalDate wateringDate = LocalDate.now();

        WateringRequest request = WateringRequest.builder().id(wateringId).plantId(plantId).chemicalId(chemicalId).wateringDate(wateringDate).build();
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).waterings(new ArrayList<Watering>()).build();
        Chemical chemical = Chemical.builder().chemicalId(chemicalId).gardener(gardener).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));
        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.of(chemical));
        when(wateringRepository.findByWateringIdAndPlant_Gardener_GardenerId(wateringId, gardenerId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(ResourceNotFoundException.class, () -> wateringCommandService.update(request, gardenerId));
    }

    @Test
    @DisplayName("물 주기 개별 삭제: 성공")
    void deleteById() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 3L;
        Long wateringId = 4L;

        Watering watering = Watering.builder().wateringId(wateringId).build();
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).waterings(new ArrayList<Watering>()).build();

        when(wateringRepository.findByWateringIdAndPlant_Gardener_GardenerId(wateringId, gardenerId))
                .thenReturn(Optional.of(watering));
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));
        when(wateringRepository.findLatestFourWateringDate(plantId)).thenReturn(new ArrayList<>());

        // When
        WateringMessage result = wateringCommandService.deleteById(wateringId, plantId, gardenerId);

        // Then
        verify(wateringRepository).delete(watering);
        assertNotNull(result);
        assertEquals(AfterWateringCode.NO_RECORD.getCode(), result.getAfterWateringCode());
    }

    @Test
    @DisplayName("물 주기 개별 삭제: 해당 item 없음 - 실패")
    void deleteById_WhenWateringNotExistOrNotMine_ShouldThrowResourceNotFoundException() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 3L;
        Long wateringId = 4L;

        when(wateringRepository.findByWateringIdAndPlant_Gardener_GardenerId(wateringId, gardenerId))
                .thenReturn(Optional.empty());

        // When, Then
        assertThrows(ResourceNotFoundException.class, () -> wateringCommandService.deleteById(wateringId, plantId, gardenerId));
        verify(wateringRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("식물에 속한 모든 물 주기 삭제: 성공")
    void deleteAllWateringsByPlantId() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Status status = Status.builder().heavyDrinker(YesOrNoType.Y).attention(YesOrNoType.Y).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).status(status).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When
        wateringCommandService.deleteByPlantId(plantId, gardenerId);

        // Then
        verify(wateringRepository).deleteAllByPlant_PlantId(plantId);

        // Plant 상태 업데이트 검증
        assertEquals(0, plant.getRecentWateringPeriod());
        assertEquals(0, plant.getEarlyWateringPeriod());
        assertEquals(YesOrNoType.N, plant.getStatus().getHeavyDrinker());
        assertEquals(YesOrNoType.Y, plant.getStatus().getAttention());
    }

    @Test
    @DisplayName("식물에 속한 모든 물 주기 삭제: 그런 식물 없음 - 실패")
    void deleteAllWateringsByPlantId_WhenPlantNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 2L;

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(ResourceNotFoundException.class, () -> wateringCommandService.deleteByPlantId(plantId, gardenerId));
        verify(wateringRepository, times(0)).deleteAllByPlant_PlantId(plantId);
    }

    @Test
    @DisplayName("식물에 속한 모든 물 주기 삭제: 내 식물 아님 - 실패")
    void deleteAllWateringsByPlantId_WhenPlantNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long plantId = 1L;
        Long requesterId = 2L;
        Long ownerId = 3L;

        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Plant plant = Plant.builder().gardener(owner).build();
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When, Then
        assertThrows(UnauthorizedAccessException.class, () -> wateringCommandService.deleteByPlantId(plantId, requesterId));
        verify(wateringRepository, times(0)).deleteAllByPlant_PlantId(plantId);
    }
}