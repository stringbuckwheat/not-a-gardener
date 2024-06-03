package xyz.notagardener.repot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.repot.Repot;
import xyz.notagardener.repot.dto.RepotRequest;
import xyz.notagardener.repot.dto.RepotResponse;
import xyz.notagardener.repot.repository.RepotRepository;
import xyz.notagardener.status.PlantStatus;
import xyz.notagardener.status.PlantStatusResponse;
import xyz.notagardener.status.PlantStatusType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("분갈이 컴포넌트 테스트")
class RepotServiceImplTest {
    @Mock
    private PlantRepository plantRepository;

    @Mock
    private RepotRepository repotRepository;

    @Mock
    private RepotStatusService repotStatusService;

    @InjectMocks
    private RepotServiceImpl repotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    static Stream<String> provideInitPeriodCondition() {
        Gardener owner = Gardener.builder().gardenerId(99L).build();
        Plant plant = Plant.builder().gardener(owner).build();

        return Stream.of("Y", "N");
    }

    @ParameterizedTest
    @MethodSource("provideInitPeriodCondition")
    @DisplayName("분갈이 기록 추가: 성공 - 물주기 간격 초기화")
    void addOne_WhenInitPeriod_ShouldReturnRepotResponse(String initCondition) {
        // Given
        Long plantId = 1L;
        Long gardenerId = 2L;

        RepotRequest request = RepotRequest.builder().plantId(plantId).repotDate(LocalDate.now()).haveToInitPeriod(initCondition).build();
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).recentWateringPeriod(7).gardener(gardener).build();
        PlantStatus status = PlantStatus.builder()
                .status(PlantStatusType.JUST_REPOTTED.getType())
                .recordedDate(request.getRepotDate())
                .plant(plant)
                .build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));
        when(repotRepository.save(any())).thenReturn(new Repot(3L, request.getRepotDate(), "Y" ,plant));
        when(repotStatusService.handleRepotStatus(request, plant)).thenReturn(new PlantStatusResponse(status));

        // When
        RepotResponse result = repotService.addOne(request, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(plantId, request.getPlantId());
        assertEquals(PlantStatusType.JUST_REPOTTED.getType(), result.getStatus().getStatus());
        assertEquals("Y".equals(initCondition) ? 0 : plant.getRecentWateringPeriod(), plant.getRecentWateringPeriod());
    }

    static Stream<Arguments> provideSaveFailureScenarios() {
        Gardener owner = Gardener.builder().gardenerId(99L).build();
        Plant plant = Plant.builder().gardener(owner).build();

        return Stream.of(
                // 그런 식물 없음
                Arguments.of(Optional.empty(), ResourceNotFoundException.class),

                // 내 식물 아님
                Arguments.of(Optional.of(plant), UnauthorizedAccessException.class)
        );
    }

    @DisplayName("분갈이 기록 추가: 실패 시나리오")
    @ParameterizedTest
    @MethodSource("provideSaveFailureScenarios")
    void addOne_WhenInvalid_ShouldThrowExceptions(Optional<Plant> plant, Class<? extends Exception> expectedException) {
        // Given
        Long requesterId = 1L;
        Long plantId = 2L;

        RepotRequest request = RepotRequest.builder().plantId(plantId).repotDate(LocalDate.now()).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(plant);

        // When, Then
        assertThrows(expectedException, () -> repotService.addOne(request, requesterId));
    }


    @DisplayName("분갈이 기록 여러 개 추가: 실패 시나리오")
    @ParameterizedTest
    @MethodSource("provideSaveFailureScenarios")
    void addSeveral_WhenInvalid_ShouldThrowExceptions(Optional<Plant> plant, Class<? extends Exception> expectedException) {
        // Given
        Long gardenerId = 1L;
        RepotRequest request = RepotRequest.builder().plantId(2L).repotDate(LocalDate.now()).build();

        when(plantRepository.findByPlantId(anyLong())).thenReturn(plant);

        // When, Then
        assertThrows(expectedException, () -> repotService.addOne(request, gardenerId));
    }

    @Test
    @DisplayName("분갈이 기록 수정: 성공")
    void update_ShouldReturnRepotResponse() {
        // Given
        Long gardenerId = 1L;
        Long repotId = 2L;
        LocalDate newRepotDate = LocalDate.now();

        RepotRequest repotRequest = RepotRequest.builder().repotId(repotId).plantId(3L).repotDate(LocalDate.now()).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().gardener(gardener).plantId(3L).build();

        // 기존 분갈이 기록
        Repot repot = new Repot(repotId, LocalDate.now().minusDays(7), "N", plant);

        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.of(repot));

        // When
        RepotResponse response = repotService.update(repotRequest, gardenerId);

        // Then
        assertNotNull(response);
        assertEquals(repotId, response.getRepotId());
        assertEquals(newRepotDate, response.getRepotDate());
        verify(repotRepository, times(1)).findByRepotId(repotId);
    }

    @Test
    @DisplayName("분갈이 기록 수정: 그런 분갈이 기록 없음")
    void update_WhenNoSuchRepot_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 100L;
        Long repotId = 1L;
        LocalDate newRepotDate = LocalDate.now();

        RepotRequest repotRequest = RepotRequest.builder().repotId(repotId).plantId(3L).repotDate(LocalDate.now()).build();
        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            repotService.update(repotRequest, gardenerId);
        });

        assertEquals(ExceptionCode.NO_SUCH_REPOT, exception.getCode());
        verify(repotRepository, times(1)).findByRepotId(repotId);
        verify(repotRepository, times(0)).save(any()); // 저장이 호출되지 않았는지 확인
    }

    @Test
    @DisplayName("분갈이 기록 수정: 내 분갈이 기록이 아님 - 실패")
    void update_WhenRepotNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long gardenerId = 100L;
        Long repotId = 1L;
        LocalDate newRepotDate = LocalDate.now();

        RepotRequest repotRequest = RepotRequest.builder().repotId(repotId).plantId(3L).repotDate(LocalDate.now()).build();

        Gardener owner = Gardener.builder().gardenerId(99L).build();
        Plant plant = Plant.builder().gardener(owner).plantId(3L).build();

        Repot repot = new Repot(repotId, LocalDate.now().minusDays(7), "Y", plant);

        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.of(repot));

        // When, Then
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            repotService.update(repotRequest, gardenerId);
        });

        assertEquals(ExceptionCode.NOT_YOUR_REPOT, exception.getCode());
        verify(repotRepository, times(1)).findByRepotId(repotId);
        verify(repotRepository, times(0)).save(any()); // 저장이 호출되지 않았는지 확인
    }


    @Test
    @DisplayName("삭제: 성공")
    void delete() {
        // Given
        Long repotId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().gardener(gardener).build();
        Repot repot = Repot.builder().repotId(repotId).plant(plant).build();

        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.of(repot));
        doNothing().when(repotRepository).delete(repot);

        // Then
        assertDoesNotThrow(() -> repotService.delete(repotId, gardenerId));

        verify(repotRepository, times(1)).findByRepotId(repotId);
        verify(repotRepository, times(1)).delete(repot);
    }

    @Test
    @DisplayName("삭제: 분갈이 기록이 존재하지 않음")
    void delete_WhenRepotNotExist_ThrowResourceNotFoundException() {
        // Given
        Long repotId = 1L;
        Long gardenerId = 2L;

        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.empty());

        // When, Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> repotService.delete(repotId, gardenerId));

        assertEquals(ExceptionCode.NO_SUCH_REPOT, exception.getCode());

        verify(repotRepository, times(1)).findByRepotId(repotId);
        verify(repotRepository, never()).delete(any());
    }

    @Test
    @DisplayName("삭제: 내 분갈이 기록 아님 아님 - 실패")
    void delete_WhenRepotNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long repotId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(99L).build();
        Plant plant = Plant.builder().gardener(gardener).build();
        Repot repot = Repot.builder().repotId(repotId).plant(plant).build();

        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.of(repot));

        // When, Then
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
                () -> repotService.delete(repotId, gardenerId));

        assertEquals(ExceptionCode.NOT_YOUR_REPOT, exception.getCode());

        verify(repotRepository, times(1)).findByRepotId(repotId);
        verify(repotRepository, never()).delete(any());
    }
}