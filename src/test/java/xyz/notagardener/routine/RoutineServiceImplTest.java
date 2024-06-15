package xyz.notagardener.routine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.routine.dto.RoutineComplete;
import xyz.notagardener.routine.dto.RoutineMain;
import xyz.notagardener.routine.dto.RoutineRequest;
import xyz.notagardener.routine.dto.RoutineResponse;
import xyz.notagardener.routine.repository.RoutineRepository;
import xyz.notagardener.routine.service.RoutineServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("루틴 컴포넌트 테스트")
class RoutineServiceImplTest {
    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private GardenerRepository gardenerRepository;

    @InjectMocks
    private RoutineServiceImpl routineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("전체 루틴 리스트: 성공")
    void getAll_ShouldReturnRoutineList() {
        // Given
        Long gardenerId = 1L;

        Plant plant = Plant.builder().build();
        List<Routine> routines = Arrays.asList(
                // 오늘 완료
                Routine.builder().routineId(1L).plant(plant).lastCompleteDate(LocalDate.now()).cycle(1).build(),
                // 오늘 해야하는데 안함
                Routine.builder().routineId(2L).plant(plant).lastCompleteDate(LocalDate.now().minusDays(1)).cycle(1).build(),
                // 완료 이력 없음
                Routine.builder().routineId(3L).plant(plant).lastCompleteDate(null).cycle(2).build(),
                // 오늘 안 해도 됨
                Routine.builder().routineId(4L).plant(plant).lastCompleteDate(LocalDate.now().minusDays(3)).cycle(5).build()
        );

        when(routineRepository.findByGardener_GardenerId(gardenerId)).thenReturn(routines);

        // When
        RoutineMain result = routineService.getAll(gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getNotToDoList().size());
        assertEquals(3, result.getTodoList().size());
        assertEquals(1, result.getTodoList().stream().filter(r -> r.getIsCompleted().equals("Y")).toList().size());
        assertEquals(2, result.getTodoList().stream().filter(r -> r.getIsCompleted().equals("N")).toList().size());
    }

    @Test
    @DisplayName("루틴 추가: 성공")
    void add_ShouldReturnSavedRoutine() {
        // Given
        Long gardenerId = 1L;
        Long plantId = 2L;
        RoutineRequest request = RoutineRequest.builder().content("루틴 추가").cycle(7).plantId(plantId).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).build();
        Routine routine = Routine.builder()
                .routineId(1L)
                .plant(plant)
                .content(request.getContent())
                .lastCompleteDate(null)
                .cycle(request.getCycle())
                .gardener(gardener)
                .createDate(LocalDate.now())
                .build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));
        when(routineRepository.save(any())).thenReturn(routine);

        // When
        RoutineResponse result = routineService.add(request, gardenerId);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    @DisplayName("루틴 추가: 실패 - 그런 식물 없음")
    void add_WhenPlantNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long plantId = 3L;
        RoutineRequest request = RoutineRequest.builder().content("루틴 추가").cycle(7).plantId(plantId).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> routineService.add(request, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_PLANT, e.getCode());
    }

    @Test
    @DisplayName("루틴 추가: 실패 - 내 식물이 아님")
    void add_WhenPlantNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long gardenerId = 1L;
        Long ownerId = 2L;
        Long plantId = 3L;
        RoutineRequest request = RoutineRequest.builder().content("루틴 추가").cycle(7).plantId(plantId).build();

        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Gardener requester = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(owner).build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(requester);
        when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));

        // When, Then
        Executable executable = () -> routineService.add(request, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLANT, e.getCode());
    }

    @Test
    @DisplayName("루틴 수정: 성공")
    void update_ShouldReturnUpdatedRoutineDto() {
        // Given
        Long gardenerId = 1L;
        Long prevPlantId = 2L;
        Long newPlantId = 3L;
        Long routineId = 4L;
        RoutineRequest request = RoutineRequest.builder().id(routineId).content("루틴 수정").cycle(7).plantId(newPlantId).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant prevPlant = Plant.builder().plantId(prevPlantId).gardener(gardener).build();
        Plant newPlant = Plant.builder().plantId(newPlantId).gardener(gardener).build();

        Routine routine = Routine.builder()
                .routineId(routineId)
                .plant(prevPlant) // 수정 가능
                .content(request.getContent()) // 수정 가능
                .lastCompleteDate(LocalDate.now().minusDays(3))
                .cycle(request.getCycle()) // 수정 가능
                .gardener(gardener)
                .createDate(LocalDate.now())
                .build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findById(newPlantId)).thenReturn(Optional.of(newPlant));
        when(routineRepository.findByRoutineId(routineId)).thenReturn(Optional.of(routine));

        // When
        RoutineResponse result = routineService.update(request, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getContent(), result.getContent());
        assertEquals(request.getPlantId(), result.getPlantId());
        assertEquals(request.getCycle(), result.getCycle());
    }

    @Test
    @DisplayName("루틴 수정: 그런 식물 없음 - 실패")
    void update_WhenPlantNotMine_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L; // 요청자
        Long plantId = 2L; // 요청
        RoutineRequest request = RoutineRequest.builder().id(4L).content("루틴 수정").cycle(7).plantId(plantId).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build(); // 식물 소유자

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> routineService.update(request, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_PLANT, e.getCode());
    }

    @Test
    @DisplayName("루틴 수정: 내 식물이 아님 - 실패")
    void update_WhenPlantNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long gardenerId = 1L; // 요청자
        Long plantId = 2L; // 요청
        Long plantOwnerId = 3L; // 실제 식물 주인
        RoutineRequest request = RoutineRequest.builder().id(4L).content("루틴 수정").cycle(7).plantId(plantId).build();

        Gardener owner = Gardener.builder().gardenerId(plantOwnerId).build(); // 식물 소유자
        Gardener requester = Gardener.builder().gardenerId(gardenerId).build(); // 요청자
        Plant plant = Plant.builder().plantId(plantId).gardener(owner).build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(requester);
        when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));

        // When, Then
        Executable executable = () -> routineService.update(request, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLANT, e.getCode());
    }

    @Test
    @DisplayName("루틴 수정: 그런 루틴 없음 - 실패")
    void update_WhenRoutineNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L; // 요청자
        Long plantId = 2L;
        Long routineId = 3L;
        RoutineRequest request = RoutineRequest.builder().id(routineId).content("루틴 수정").cycle(7).plantId(plantId).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));
        when(routineRepository.findByRoutineId(routineId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> routineService.update(request, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_ROUTINE, e.getCode());
    }

    @Test
    @DisplayName("루틴 수정: 내 루틴이 아님 - 실패")
    void update_WhenRoutineNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long gardenerId = 1L; // 요청자
        Long ownerId = 2L;
        Long plantId = 3L;
        Long routineId = 4L;
        RoutineRequest request = RoutineRequest.builder().id(routineId).content("루틴 수정").cycle(7).plantId(plantId).build();

        Gardener requester = Gardener.builder().gardenerId(gardenerId).build();
        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(requester).build();

        Routine routine = Routine.builder()
                .routineId(routineId)
                .plant(plant) // 수정 가능
                .content(request.getContent()) // 수정 가능
                .lastCompleteDate(LocalDate.now().minusDays(3))
                .cycle(request.getCycle()) // 수정 가능
                .gardener(owner)
                .createDate(LocalDate.now())
                .build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(requester);
        when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));
        when(routineRepository.findByRoutineId(routineId)).thenReturn(Optional.of(routine));

        // When, Then
        Executable executable = () -> routineService.update(request, gardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_ROUTINE, e.getCode());
    }

    @Test
    @DisplayName("루틴 완료: 성공")
    void complete_ShouldReturnCompletedRoutineDto() {
        // Given
        Long gardenerId = 1L; // 요청자
        Long routineId = 2L;
        RoutineComplete request = new RoutineComplete(routineId, LocalDate.now());

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = Plant.builder().plantId(3L).gardener(gardener).build();
        Routine routine = Routine.builder().routineId(routineId).plant(plant).gardener(gardener).createDate(LocalDate.now()).build();

        when(routineRepository.findByRoutineId(routineId)).thenReturn(Optional.of(routine));

        // When
        RoutineResponse result = routineService.complete(request, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(routineId, result.getId());
        assertEquals(request.getLastCompleteDate(), result.getLastCompleteDate());
    }

    @Test
    @DisplayName("루틴 완료: 내 루틴이 아님 - 실패")
    void complete_WhenRoutineNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long requesterId = 1L;
        Long ownerId = 2L;
        Long routineId = 3L;

        RoutineComplete request = new RoutineComplete(routineId, LocalDate.now());

        Gardener owner = Gardener.builder().gardenerId(ownerId).build();
        Plant plant = Plant.builder().plantId(3L).build();
        Routine routine = Routine.builder().routineId(routineId).plant(plant).gardener(owner).createDate(LocalDate.now()).build();

        when(routineRepository.findByRoutineId(routineId)).thenReturn(Optional.of(routine));

        // When, Then
        Executable executable = () -> routineService.complete(request, requesterId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_ROUTINE, e.getCode());
    }

    @Test
    @DisplayName("루틴 완료: 그런 루틴 없음 - 실패")
    void complete_WhenRoutineNotMine_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long routineId = 2L;

        RoutineComplete request = new RoutineComplete(routineId, LocalDate.now());

        when(routineRepository.findByRoutineId(routineId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> routineService.complete(request, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_ROUTINE, e.getCode());
    }

    @Test
    @DisplayName("삭제: 성공")
    void delete() {
        // Given
        Long routineId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Routine routine = Routine.builder().routineId(routineId).gardener(gardener).createDate(LocalDate.now()).build();

        when(routineRepository.findByRoutineId(routineId)).thenReturn(Optional.of(routine));
        doNothing().when(routineRepository).delete(routine);

        // Then
        assertDoesNotThrow(() -> routineService.delete(routineId, gardenerId));

        verify(routineRepository, times(1)).findByRoutineId(routineId);
        verify(routineRepository, times(1)).delete(routine);
    }

    @Test
    @DisplayName("삭제: 루틴이 존재하지 않음")
    void delete_WhenRoutineNotExist_ThrowResourceNotFoundException() {
        // Given
        Long routineId = 1L;
        Long gardenerId = 2L;

        when(routineRepository.findByRoutineId(routineId)).thenReturn(Optional.empty());

        // When, Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> routineService.delete(routineId, gardenerId));

        assertEquals(ExceptionCode.NO_SUCH_ROUTINE, exception.getCode());

        verify(routineRepository, times(1)).findByRoutineId(routineId);
        verify(routineRepository, never()).delete(any());
    }

    @Test
    @DisplayName("삭제: 내 루틴 아님 - 실패")
    void delete_WhenRoutineNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long routineId = 1L;
        Long gardenerId = 2L;

        Gardener owner = Gardener.builder().gardenerId(3L).build();
        Routine routine = Routine.builder().routineId(routineId).gardener(owner).createDate(LocalDate.now()).build();

        when(routineRepository.findByRoutineId(routineId)).thenReturn(Optional.of(routine));

        // When, Then
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
                () -> routineService.delete(routineId, gardenerId));

        assertEquals(ExceptionCode.NOT_YOUR_ROUTINE, exception.getCode());

        verify(routineRepository, times(1)).findByRoutineId(routineId);
        verify(routineRepository, never()).delete(any());
    }
}