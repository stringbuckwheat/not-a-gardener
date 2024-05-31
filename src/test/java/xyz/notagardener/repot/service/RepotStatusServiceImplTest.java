package xyz.notagardener.repot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.repot.dto.RepotRequest;
import xyz.notagardener.status.PlantStatus;
import xyz.notagardener.status.PlantStatusRepository;
import xyz.notagardener.status.PlantStatusResponse;
import xyz.notagardener.status.PlantStatusType;
import xyz.notagardener.watering.watering.repository.WateringRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("분갈이 기록 후 식물 상태 변경 컴포넌트 테스트")
class RepotStatusServiceImplTest {
    @Mock
    private WateringRepository wateringRepository;

    @Mock
    private PlantStatusRepository plantStatusRepository;

    @InjectMocks
    private RepotStatusServiceImpl repotStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("분갈이 날짜 이후 물주기 기록 있음")
    void handleRepotStatus_WhenWateringRecordExistAfterRepotDate_ShouldReturnEmptyPlantStatus() {
        // Given
        RepotRequest request = RepotRequest.builder().repotDate(LocalDate.now().minusDays(10)).build();
        Plant plant = Plant.builder().plantId(1L).build();

        when(wateringRepository.existsByPlant_PlantIdAndWateringDateAfter(request.getPlantId(), request.getRepotDate()))
                .thenReturn(true);

        // When
        PlantStatusResponse result = repotStatusService.handleRepotStatus(request, plant);

        // Then
        assertNotNull(result);
        assertNull(result.getPlantStatusId());
        assertNull(result.getStatus());
    }

    @Test
    @DisplayName("분갈이 날짜 이후 물주기 기록 없음")
    void handleRepotStatus_WhenWateringRecordNotExistAfterRepotDate_ShouldReturnPlantStatus() {
        // Given
        RepotRequest request = RepotRequest.builder().repotDate(LocalDate.now()).build();
        Plant plant = Plant.builder().plantId(1L).build();

        PlantStatus status = PlantStatus.builder()
                .plantStatusId(2L)
                .status(PlantStatusType.JUST_REPOTTED.getType())
                .recordedDate(request.getRepotDate())
                .plant(plant)
                .build();

        when(wateringRepository.existsByPlant_PlantIdAndWateringDateAfter(request.getPlantId(), request.getRepotDate()))
                .thenReturn(false);
        when(plantStatusRepository.save(any())).thenReturn(status);

        // When
        PlantStatusResponse result = repotStatusService.handleRepotStatus(request, plant);

        // Then
        assertNotNull(result);
        assertNotNull(result.getPlantStatusId());
        assertEquals(PlantStatusType.JUST_REPOTTED.getType(), result.getStatus());
        assertEquals(request.getRepotDate(), result.getRecordedDate());
        assertEquals(plant.getPlantId(), result.getPlantId());
    }
}