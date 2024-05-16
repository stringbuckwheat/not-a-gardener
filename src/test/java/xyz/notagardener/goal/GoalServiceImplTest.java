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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.gardener.GardenerRepository;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.PlantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DisplayName("목표")
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
    void getPlantForGoal_WhenPlantNotExist_ThrowsNoSuchElementException() {
        // Given
        Long plantId = 1L;
        Long gardenerId = 2L;

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> goalService.getPlantForGoal(plantId, gardenerId);
        NoSuchElementException e = assertThrows(NoSuchElementException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_ITEM.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("목표 추가/수정용 식물 가져오기: 내 식물이 아닌 경우 - 실패")
    void getPlantForGoal_WhenPlantNotMine_ThrowsNoSuchElementException() {
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
        assertEquals(ExceptionCode.NOT_YOUR_THING.getCode(), e.getMessage());
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

        GoalDto request = GoalDto.builder().content("식물 목표").complete("N").plantId(plantId).plantName("샌더소니").build();

        Goal savedGoal = Goal.builder()
                .goalId(1L)
                .content(request.getContent())
                .complete("N")
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

        GoalDto request = GoalDto.builder().content("식물 목표").complete("N").plantId(0L).plantName("샌더소니").build();

        Goal savedGoal = Goal.builder()
                .goalId(1L)
                .content(request.getContent())
                .complete("N")
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

    static Stream<GoalDto> invalidGoalSavingData() {
        String validContent = "식물 목표 추가";
        String validComplete = "N";
        Long validPlantId = 2L;

        return Stream.of(
                GoalDto.builder().content(null).complete(validComplete).plantId(validPlantId).build(),
                GoalDto.builder().content("").complete(validComplete).plantId(validPlantId).build(),
                GoalDto.builder().content("reallyreallyreallyreallyreallyreallyreallyreallylongcontentreallyreallyreallyreallyreallyreallyreallyreallylongcontent").complete(validComplete).plantId(validPlantId).build(),

                GoalDto.builder().content(validContent).complete("메밀").plantId(validPlantId).build(),
                GoalDto.builder().content(validContent).complete(null).plantId(validPlantId).build(),
                GoalDto.builder().content(validContent).complete("").plantId(validPlantId).build(),
                GoalDto.builder().content(validContent).complete("Y").plantId(validPlantId).build(),

                GoalDto.builder().content(validContent).complete(validComplete).plantId(-1L).build()
        );
    }

    @ParameterizedTest
    @MethodSource("invalidGoalSavingData")
    @DisplayName("목표 추가: 실패 - 입력값 유효성 검사 실패")
    void add_WhenInputDataInvalid_ThrowIllegalArgumentException(GoalDto invalidInput) {
        // Given
        Long gardenerId = 1L;

        // When, Then
        Executable executable = () -> goalService.add(gardenerId, invalidInput);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(ExceptionCode.INVALID_REQUEST_DATA.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("목표 추가: 실패 - gardenerId 오류")
    void add_WhenGardenerNotExist_ThrowNoSuchElementException() {
        // Given
        Long gardenerId = 10L;
        GoalDto request = GoalDto.builder().content("식물 목표").complete("N").plantId(1L).plantName("샌더소니").build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(null);

        // When, Then
        Executable executable = () -> goalService.add(gardenerId, request);
        UsernameNotFoundException e = assertThrows(UsernameNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_ACCOUNT.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("목표 추가: 실패 - plantId에 해당하는 식물 없음")
    void add_WhenPlantNotExist_ThrowNoSuchElementException() {
        // Given
        Long gardenerId = 1L;
        Long plantId = 10L;
        GoalDto request = GoalDto.builder().content("식물 목표").complete("N").plantId(plantId).plantName("샌더소니").build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findByPlantIdAndGardener_GardenerId(plantId, gardenerId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> goalService.add(gardenerId, request);
        NoSuchElementException e = assertThrows(NoSuchElementException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_ITEM.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("목표 추가: 실패 - 내 식물 아님")
    void add_WhenPlantNotMine_ThrowNoSuchElementException() {
        // Given
        Long gardenerId = 1L;
        Long plantId = 10L;
        GoalDto request = GoalDto.builder().content("식물 목표").complete("N").plantId(plantId).plantName("샌더소니").build();

        Gardener gardener = Gardener.builder().gardenerId(2L).build();
        Plant plant = Plant.builder().plantId(plantId).gardener(gardener).build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(plant));

        // When, Then
        Executable executable = () -> goalService.add(gardenerId, request);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_THING.getCode(), e.getMessage());
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

        GoalDto request = GoalDto.builder().id(goalId).content(newContent).complete("N").plantId(newPlantId).build();

        Goal goal = Goal.builder()
                .goalId(goalId)
                .content(prevContent)
                .complete("N")
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

    static Stream<GoalDto> invalidGoalUpdateData() {
        Long validGoalId = 1L;
        String validContent = "식물 목표 수정";
        String validComplete = "N";
        Long validPlantId = 2L;

        return Stream.of(
                GoalDto.builder().id(validGoalId).content(null).complete(validComplete).plantId(validPlantId).build(),
                GoalDto.builder().id(validGoalId).content("").complete(validComplete).plantId(validPlantId).build(),
                GoalDto.builder().id(validGoalId).content("reallyreallyreallyreallyreallyreallyreallyreallylongcontentreallyreallyreallyreallyreallyreallyreallyreallylongcontent").complete(validComplete).plantId(validPlantId).build(),

                GoalDto.builder().id(validGoalId).content(validContent).complete("T").plantId(validPlantId).build(),
                GoalDto.builder().id(validGoalId).content(validContent).complete(null).plantId(validPlantId).build(),
                GoalDto.builder().id(validGoalId).content(validContent).complete("").plantId(validPlantId).build(),

                GoalDto.builder().id(validGoalId).content(validContent).complete(validComplete).plantId(-1L).build()
        );
    }

    @ParameterizedTest
    @MethodSource("invalidGoalUpdateData")
    @DisplayName("목표 수정: 실패 - 입력값 유효성 검증 실패")
    void update_WhenRequestDataInvalid_ThrowIllegalArgumentException(GoalDto invalidInput) {
        // Given
        Long gardenerId = 1L;

        // When, Then
        Executable executable = () -> goalService.update(gardenerId, invalidInput);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(ExceptionCode.INVALID_REQUEST_DATA.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("목표 수정: 실패 - plantId에 해당하는 식물 없음")
    void update_WhenPlantNotExist_ThrowNoSuchElementException() {
        // Given
        Long gardenerId = 1L;
        Long goalId = 1L;
        Long newPlantId = 2L;

        GoalDto request = GoalDto.builder().id(goalId).content("목표 수정").complete("N").plantId(newPlantId).build();

        when(plantRepository.findByPlantId(newPlantId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> goalService.update(gardenerId, request);
        NoSuchElementException e = assertThrows(NoSuchElementException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_ITEM.getCode(), e.getMessage());
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

        GoalDto request = GoalDto.builder().id(goalId).content("목표 수정").complete("N").plantId(newPlantId).build();

        when(plantRepository.findByPlantId(newPlantId)).thenReturn(Optional.of(plant));

        // When, Then
        Executable executable = () -> goalService.update(invalidGardenerId, request);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_THING.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("목표 수정: 실패 - 수정 대상인 목표가 존재하지 않음")
    void update_WhenGoalNotExist_ThrowNoSuchElementException() {
        // Given
        Long gardenerId = 1L;
        Long goalId = 1L;

        Long newPlantId = 2L;
        String newContent = "새로운 식물 목표";

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Plant newPlant = Plant.builder().plantId(newPlantId).gardener(gardener).build();

        GoalDto request = GoalDto.builder().id(goalId).content(newContent).complete("N").plantId(newPlantId).build();

        when(plantRepository.findByPlantId(newPlantId)).thenReturn(Optional.of(newPlant));
        when(goalRepository.findByGoalId(goalId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> goalService.update(gardenerId, request);
        NoSuchElementException e = assertThrows(NoSuchElementException.class, executable);
        assertEquals(ExceptionCode.NO_SUCH_ITEM.getCode(), e.getMessage());
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

        GoalDto request = GoalDto.builder().id(goalId).content("새로운 식물 목표").complete("N").plantId(plantId).build();
        Goal goal = Goal.builder().goalId(goalId).gardener(actualGardener).build();

        when(plantRepository.findByPlantId(plantId)).thenReturn(Optional.of(newPlant));
        when(goalRepository.findByGoalId(goalId)).thenReturn(Optional.of(goal));

        // When, Then
        Executable executable = () -> goalService.update(invalidGardenerId, request);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_THING.getCode(), e.getMessage());
    }

    static Stream<String> getPrevComplete() {
        return Stream.of("Y", "N");
    }

    @ParameterizedTest
    @MethodSource("getPrevComplete")
    void complete_WhenPrevGoalIncomplete_ReturnCompletedGoalDto(String prevComplete) {
        // Given
        Long goalId = 1L;
        Long gardenerId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build(); // 목표의 소유자
        Goal goal = Goal.builder().goalId(goalId).complete(prevComplete).gardener(gardener).build();

        when(goalRepository.findByGoalId(goalId)).thenReturn(Optional.of(goal));

        // When
        GoalDto result = goalService.complete(goalId, gardenerId);

        // Then
        String expected = prevComplete.equals("Y") ? "N" : "Y";
        assertEquals(expected, result.getComplete());
    }

    @Test
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