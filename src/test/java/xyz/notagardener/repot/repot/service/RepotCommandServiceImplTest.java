package xyz.notagardener.repot.repot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.repot.Repot;
import xyz.notagardener.repot.repot.dto.RepotRequest;
import xyz.notagardener.repot.repot.repository.RepotRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("분갈이 등록 컴포넌트 테스트")
class RepotCommandServiceImplTest {
    @Mock
    private RepotRepository repotRepository;

    @Mock
    private PlantRepository plantRepository;

    @InjectMocks
    private RepotCommandServiceImpl repotCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Repot 가져오기: 성공")
    void getRepotByRepotIdAndGardenerId_WhenValid_ShouldReturnRepot() {
        Long gardenerId = 1L;
        Long repotId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().gardener(gardener).build();
        Repot repot = Repot.builder().repotId(repotId).plant(plant).build();

        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.of(repot));

        Repot result = repotCommandService.getRepotByRepotIdAndGardenerId(repotId, gardenerId);

        assertEquals(repotId, result.getRepotId());
    }

    @Test
    @DisplayName("Repot 가져오기: 그런 분갈이 정보 없음")
    void getRepotByRepotIdAndGardenerId_WhenRepotNotExist_ShouldThrowResourceNotFoundException() {
        when(repotRepository.findByRepotId(anyLong())).thenReturn(Optional.empty());

        Executable executable = () -> repotCommandService.getRepotByRepotIdAndGardenerId(1L, 2L);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_REPOT, e.getCode());
    }

    @Test
    @DisplayName("Repot 가져오기: 내 분갈이 정보가 아님")
    void getRepotByRepotIdAndGardenerId_WhenRepotNotMine_ShouldThrowUnauthorizedAccessException() {
        Long gardenerId = 1L;
        Long repotId = 2L;

        Long ownerId = 3L;

        Gardener gardener = Gardener.builder().gardenerId(ownerId).build();
        Plant plant = Plant.builder().gardener(gardener).build();
        Repot repot = Repot.builder().repotId(repotId).plant(plant).build();

        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.of(repot));

        Executable executable = () -> repotCommandService.getRepotByRepotIdAndGardenerId(repotId, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_REPOT, e.getCode());
    }

    static Stream<YesOrNoType> provideInitPeriod() {
        return Stream.of(YesOrNoType.Y, YesOrNoType.N);
    }

    @ParameterizedTest
    @MethodSource("provideInitPeriod")
    @DisplayName("분갈이 기록 추가")
    void addOne(YesOrNoType initCondition) {
        // Given
        Long plantId = 1L;
        Long gardenerId = 2L;

        RepotRequest request = RepotRequest.builder().plantId(plantId).repotDate(LocalDate.now()).haveToInitPeriod(initCondition).build();
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).recentWateringPeriod(7).gardener(gardener).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));
        when(repotRepository.save(any())).thenReturn(new Repot(3L, request.getRepotDate(), YesOrNoType.Y, plant));

        // When
        Repot result = repotCommandService.addOne(request, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(plantId, result.getPlant().getPlantId());
        assertEquals(YesOrNoType.Y.equals(initCondition) ? 0 : plant.getRecentWateringPeriod(), plant.getRecentWateringPeriod());
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
        Repot repot = new Repot(repotId, LocalDate.now().minusDays(7), YesOrNoType.N, plant);

        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.of(repot));

        // When
        Repot result = repotCommandService.update(repotRequest, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(repotId, result.getRepotId());
        assertEquals(newRepotDate, result.getRepotDate());
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
            repotCommandService.update(repotRequest, gardenerId);
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

        RepotRequest repotRequest = RepotRequest.builder().repotId(repotId).plantId(3L).repotDate(LocalDate.now()).build();

        Gardener gardener = Gardener.builder().gardenerId(1L).build();
        Plant plant = Plant.builder().gardener(gardener).plantId(3L).build();

        // 기존 분갈이 기록
        Repot repot = new Repot(repotId, LocalDate.now().minusDays(7), YesOrNoType.N, plant);

        when(repotRepository.findByRepotId(repotId)).thenReturn(Optional.of(repot));

        // When, Then
        assertThrows(UnauthorizedAccessException.class, () -> repotCommandService.update(repotRequest, gardenerId));
    }
}