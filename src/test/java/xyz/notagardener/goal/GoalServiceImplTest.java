package xyz.notagardener.goal;

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
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.goal.dto.GoalDto;
import xyz.notagardener.goal.repository.GoalRepository;
import xyz.notagardener.goal.service.GoalServiceImpl;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DisplayName("목표 컴포넌트 테스트")
class GoalServiceImplTest {
    @Mock
    private GoalRepository goalRepository;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private GardenerRepository gardenerRepository;

    @InjectMocks
    private GoalServiceImpl goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("목표 추가/수정용 식물 가져오기: 성공")
    void getPlantForGoal_WhenValid_ReturnGardenersPlant() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).name("메밀").build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When
        Plant result = goalService.getPlantForGoal(plantId, gardenerId);

        // Then
        assertEquals(plantId, result.getPlantId());
        assertEquals(gardenerId, result.getGardener().getGardenerId());
    }

    @Test
    @DisplayName("목표 추가/수정용 식물 가져오기: plantId에 해당하는 식물이 없는 경우 - 실패")
    void getPlantForGoal_WhenPlantNotExist_ThrowsResourceNotFoundException() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 2L;

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> goalService.getPlantForGoal(plantId, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_PLANT, e.getCode());
    }

    @Test
    @DisplayName("목표 추가/수정용 식물 가져오기: 내 식물이 아닌 경우 - 실패")
    void getPlantForGoal_WhenPlantNotMine_ThrowsResourceNotFoundException() {
        // Given
        Long plantId = 1L;
        Long invalidGardenerId = 2L; // 제출된 값
        Long actualGardenerId = 3L; // 실 소유주

        Gardener gardener = Gardener.builder().gardenerId(actualGardenerId).name("메밀").build(); // 실 소유주
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When, Then
        Executable executable = () -> goalService.getPlantForGoal(plantId, invalidGardenerId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLANT, e.getCode());
    }

    @Test
    @DisplayName("한 회원의 전체 목표: 성공")
    void getAll_WhenGardenerIdValid_ReturnGoalDtos() {
        // Given
        Long gardenerId = 1L;

        List<Goal> goals = new ArrayList<>();
        goals.add(Goal.builder().build());
        goals.add(Goal.builder().build());

        when(goalRepository.findByGardener_GardenerId(gardenerId)).thenReturn(goals);

        // When
        List<GoalDto> result = goalService.getAll(gardenerId);

        assertEquals(goals.size(), result.size());
    }

    @Test
    @DisplayName("목표 추가: 성공")
    void add_WhenRequestDataValid_ReturnSavedGoalDto() {
        // Given
        Long gardenerId = 1L;
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();

        Long plantId = 1L;
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).build();

        GoalDto request = GoalDto.builder().content("식물 목표").complete(YesOrNoType.N).plantId(plantId).plantName("샌더소니").build();

        Goal savedGoal = Goal.builder()
                .goalId(1L)
                .content(request.getContent())
                .complete(YesOrNoType.N)
                .plant(plant)
                .gardener(gardener)
                .build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));
        when(goalRepository.save(any())).thenReturn(savedGoal);

        // When
        GoalDto result = goalService.add(gardenerId, request);

        // Then
        assertNotNull(result.getId());
        assertEquals(request.getContent(), result.getContent());
        assertEquals(plantId, result.getPlantId());
    }

    @Test
    @DisplayName("목표 추가: 성공 - 식물(nullable) 입력하지 않은 경우")
    void add_WhenPlantDataNotExist_ReturnSavedGoalDto() {
        // Given
        Long gardenerId = 1L;
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant plant = null;

        GoalDto request = GoalDto.builder().content("식물 목표").complete(YesOrNoType.N).plantId(0L).plantName("샌더소니").build();

        Goal savedGoal = Goal.builder()
                .goalId(1L)
                .content(request.getContent())
                .complete(YesOrNoType.N)
                .plant(plant)
                .gardener(gardener)
                .build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(goalRepository.save(any())).thenReturn(savedGoal);

        // When
        GoalDto result = goalService.add(gardenerId, request);

        // Then
        assertNotNull(result.getId());
        assertEquals(request.getContent(), result.getContent());
    }

    @Test
    @DisplayName("목표 추가: 실패 - plantId에 해당하는 식물 없음")
    void add_WhenPlantNotExist_ThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long plantId = 10L;
        GoalDto request = GoalDto.builder().content("식물 목표").complete(YesOrNoType.N).plantId(plantId).plantName("샌더소니").build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findByPlantIdAndGardener_GardenerId(plantId, gardenerId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> goalService.add(gardenerId, request);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_PLANT, e.getCode());
    }

    @Test
    @DisplayName("목표 추가: 실패 - 내 식물 아님")
    void add_WhenPlantNotMine_ThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long plantId = 10L;
        GoalDto request = GoalDto.builder().content("식물 목표").complete(YesOrNoType.N).plantId(plantId).plantName("샌더소니").build();

        Gardener gardener = Gardener.builder().gardenerId(2L).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When, Then
        Executable executable = () -> goalService.add(gardenerId, request);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLANT, e.getCode());
    }

    @Test
    @DisplayName("목표 수정: 성공")
    void update_WhenSuccess_ReturnUpdatedGoalDto() {
        // Given
        Long gardenerId = 1L;
        Long goalId = 1L;

        Long newPlantId = 2L;
        Long prevPlantId = 3L;
        String newContent = "새로운 식물 목표";
        String prevContent = "기존 식물 목표";

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant prevPlant = Plant.builder().plantId(prevPlantId).gardener(gardener).build();
        Plant newPlant = Plant.builder().plantId(newPlantId).gardener(gardener).build();

        GoalDto request = GoalDto.builder().id(goalId).content(newContent).complete(YesOrNoType.N).plantId(newPlantId).build();

        Goal goal = Goal.builder()
                .goalId(goalId)
                .content(prevContent)
                .complete(YesOrNoType.N)
                .plant(prevPlant)
                .gardener(gardener)
                .build();

        when(plantRepository.findByPlantId(newPlantId)).thenReturn(Optional.of(newPlant));
        when(goalRepository.findByGoalId(goalId)).thenReturn(Optional.of(goal));

        // When
        GoalDto result = goalService.update(gardenerId, request);

        // Then
        assertEquals(goal.getGoalId(), result.getId()); // PK 같은지
        assertEquals(goal.getComplete(), result.getComplete());

        assertNotEquals(prevPlantId, result.getPlantId());
        assertNotEquals(prevContent, result.getContent());
    }

    @Test
    @DisplayName("목표 수정: 실패 - plantId에 해당하는 식물 없음")
    void update_WhenPlantNotExist_ThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long goalId = 1L;
        Long newPlantId = 2L;

        GoalDto request = GoalDto.builder().id(goalId).content("목표 수정").complete(YesOrNoType.N).plantId(newPlantId).build();

        when(plantRepository.findByPlantId(newPlantId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> goalService.update(gardenerId, request);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_PLANT, e.getCode());
    }

    @Test
    @DisplayName("목표 수정: 실패 - 내 식물이 아님")
    void update_WhenPlantNotMine_ThrowUnauthorizedAccessException() {
        // Given
        Long goalId = 1L;
        Long newPlantId = 2L;

        Long invalidGardenerId = 2L; // 제출된 값
        Long actualGardenerId = 3L; // 실 소유주

        Gardener gardener = Gardener.builder().gardenerId(actualGardenerId).name("메밀").build(); // 실 소유주
        Plant plant = Plant.builder().plantId(newPlantId).gardener(gardener).build();

        GoalDto request = GoalDto.builder().id(goalId).content("목표 수정").complete(YesOrNoType.N).plantId(newPlantId).build();

        when(plantRepository.findByPlantId(newPlantId)).thenReturn(Optional.of(plant));

        // When, Then
        Executable executable = () -> goalService.update(invalidGardenerId, request);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_PLANT, e.getCode());
    }

    @Test
    @DisplayName("목표 수정: 실패 - 수정 대상인 목표가 존재하지 않음")
    void update_WhenGoalNotExist_ThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long goalId = 1L;

        Long newPlantId = 2L;
        String newContent = "새로운 식물 목표";

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant newPlant = Plant.builder().plantId(newPlantId).gardener(gardener).build();

        GoalDto request = GoalDto.builder().id(goalId).content(newContent).complete(YesOrNoType.N).plantId(newPlantId).build();

        when(plantRepository.findByPlantId(newPlantId)).thenReturn(Optional.of(newPlant));
        when(goalRepository.findByGoalId(goalId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> goalService.update(gardenerId, request);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_GOAL, e.getCode());
    }

    @Test
    @DisplayName("목표 수정: 실패 - 내 목표가 아님")
    void update_WhenGoalNotMine_ThrowUnauthorizedAccessException() {
        // Given
        Long invalidGardenerId = 1L; // 입력값
        Long actualGardenerId = 2L; // 목표의 소유자
        Long goalId = 1L;
        Long plantId = 2L;

        Gardener invalidGardener = Gardener.builder().gardenerId(invalidGardenerId).build(); // 입력값
        Gardener actualGardener = Gardener.builder().gardenerId(actualGardenerId).build(); // 목표의 소유자
        Plant newPlant = Plant.builder().plantId(plantId).gardener(invalidGardener).build();

        GoalDto request = GoalDto.builder().id(goalId).content("새로운 식물 목표").complete(YesOrNoType.N).plantId(plantId).build();
        Goal goal = Goal.builder().goalId(goalId).gardener(actualGardener).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(newPlant));
        when(goalRepository.findByGoalId(goalId)).thenReturn(Optional.of(goal));

        // When, Then
        Executable executable = () -> goalService.update(invalidGardenerId, request);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_GOAL, e.getCode());
    }

    static Stream<YesOrNoType> getPrevComplete() {
        return Stream.of(YesOrNoType.Y, YesOrNoType.N);
    }

    @ParameterizedTest
    @MethodSource("getPrevComplete")
    @DisplayName("목표 완료")
    void complete_WhenPrevGoalIncomplete_ReturnCompletedGoalDto(YesOrNoType prevComplete) {
        // Given
        Long goalId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build(); // 목표의 소유자
        Goal goal = Goal.builder().goalId(goalId).complete(prevComplete).gardener(gardener).build();

        when(goalRepository.findByGoalId(goalId)).thenReturn(Optional.of(goal));

        // When
        GoalDto result = goalService.complete(goalId, gardenerId);

        // Then
        YesOrNoType expected = prevComplete.equals(YesOrNoType.Y) ? YesOrNoType.N : YesOrNoType.Y;
        assertEquals(expected, result.getComplete());
    }

    @Test
    @DisplayName("목표 삭제: 성공")
    void delete() {
        // Given
        Long goalId = 1L;
        Long gardenerId = 1L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build(); // 목표의 소유자
        Goal goal = Goal.builder().goalId(goalId).gardener(gardener).build();

        when(goalRepository.findByGoalId(goalId)).thenReturn(Optional.of(goal));
        doNothing().when(goalRepository).delete(goal);

        // When
        goalService.delete(goalId, gardenerId);

        // Then
        verify(goalRepository, times(1)).delete(goal);
    }
}