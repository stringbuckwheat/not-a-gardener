package xyz.notagardener.plant.garden.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.plant.garden.dto.*;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.routine.model.Routine;
import xyz.notagardener.routine.repository.RoutineRepository;
import xyz.notagardener.watering.watering.dto.ChemicalUsage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("계산된 식물 컴포넌트 테스트")
class GardenServiceImplTest {
    @Autowired
    private GardenResponseMapper gardenResponseMapper;

    @Autowired
    private GardenService gardenService;

    @MockBean
    private PlantRepository plantRepository;

    @MockBean
    private RoutineRepository routineRepository;

    @MockBean
    private ChemicalInfoServiceImpl chemicalInfoService;

    private PlantResponseFactory plantResponseFactory = new PlantResponseFactory();

    @Test
    @DisplayName("메인 페이지(물 주기, 기록 없는 식물, 루틴): 성공")
    void getGarden_WhenValid_ShouldReturnTodoAndWaitingsAndRoutines() {
        // Given
        Long gardenerId = 1L;

        int missedDay = 6;
        List<PlantResponse> plantResponses = Arrays.asList(
                plantResponseFactory.getPostponedPlant(),
                plantResponseFactory.getThirstyPlant(3),
                plantResponseFactory.getCheckingPlant(3),
                plantResponseFactory.getDryOutPlant(3, missedDay)
        );

        Plant p = Plant.builder().build();

        List<Routine> routines = Arrays.asList(
                Routine.builder().routineId(1L).plant(p).lastCompleteDate(LocalDate.now()).build(),
                Routine.builder().routineId(2L).plant(p).lastCompleteDate(null).build(),
                Routine.builder().routineId(3L).plant(p).lastCompleteDate(LocalDate.now().minusDays(2)).cycle(3).build(),
                Routine.builder().routineId(4L).plant(p).lastCompleteDate(LocalDate.now().minusDays(1)).cycle(1).build()
        );

        List<WaitingForWatering> waitings = Arrays.asList(
                new WaitingForWatering(1L, "waiting!", "species", "medium", 2L, "place", LocalDateTime.now())
        );

        when(plantRepository.existByGardenerId(gardenerId)).thenReturn(true);
        when(plantRepository.findWaitingForWateringList(gardenerId)).thenReturn(waitings);
        when(plantRepository.findGardenByGardenerId(gardenerId)).thenReturn(plantResponses);
        when(routineRepository.findByGardener_GardenerId(gardenerId)).thenReturn(routines);

        // when
        GardenMain result = gardenService.getGarden(gardenerId);

        // then
        assertNotNull(result);
        assertNotNull(result.getTodoList());
        assertNotNull(result.getWaitingList());
        assertNotNull(result.getRoutineList());

        assertFalse(result.getTodoList().isEmpty());
        assertFalse(result.getWaitingList().isEmpty());
        assertFalse(result.getRoutineList().isEmpty());

        List<String> expectedWateringCodes = Arrays.asList(
                WateringCode.YOU_ARE_LAZY.getCode(),
                WateringCode.THIRSTY.getCode(),
                WateringCode.CHECK.getCode(),
                WateringCode.LATE_WATERING.getCode()
        );

        IntStream.range(0, expectedWateringCodes.size())
                .forEach(i -> assertEquals(expectedWateringCodes.get(i), result.getTodoList().get(i).getGardenDetail().getWateringCode()));

        assertEquals(routines.size() - 1, result.getRoutineList().size());

    }

    @Test
    @DisplayName("메인 페이지(물 주기, 기록 없는 식물, 루틴): 성공")
    void getGarden_WhenPlantNotExist_ShouldReturnEmptyDtoWithFalseSign() {
        // Given
        Long gardenerId = 1L;
        when(plantRepository.existByGardenerId(gardenerId)).thenReturn(false);

        // when
        GardenMain result = gardenService.getGarden(gardenerId);

        // then
        assertNotNull(result);

        assertFalse(result.isHasPlant());
        assertNull(result.getTodoList());
        assertNull(result.getWaitingList());
        assertNull(result.getRoutineList());
    }

    @Test
    @DisplayName("전체 식물의 계산 값 반환")
    void getAll_WhenPlantExist_ShouldReturnAllPlantCalcualtedDto() {
        Long gardenerId = 1L;
        int missedDay = 5;

        List<PlantResponse> rawGardens = Arrays.asList(
                (PlantResponse) plantResponseFactory.getPostponedPlant(),
                (PlantResponse) plantResponseFactory.getPlantWithNoWateringRecord(),
                (PlantResponse) plantResponseFactory.getPlantWithOneWateringRecord(),
                (PlantResponse) plantResponseFactory.getWateredTodayPlant(),
                (PlantResponse) plantResponseFactory.getThirstyPlant(3),
                (PlantResponse) plantResponseFactory.getCheckingPlant(3),
                (PlantResponse) plantResponseFactory.getLeaveHerAlonePlant(3),
                (PlantResponse) plantResponseFactory.getDryOutPlant(3, missedDay)
        );

        when(plantRepository.findAllPlantsWithLatestWateringDate(gardenerId)).thenReturn(rawGardens);

        // when
        List<GardenResponse> result = gardenService.getAll(gardenerId);

        // then
        assertNotNull(result);
        assertEquals(rawGardens.size(), result.size());

        List<String> expectedWateringCodes = Arrays.asList(
                WateringCode.YOU_ARE_LAZY.getCode(),
                WateringCode.NOT_ENOUGH_RECORD.getCode(),
                WateringCode.NOT_ENOUGH_RECORD.getCode(),
                WateringCode.WATERED_TODAY.getCode(),
                WateringCode.THIRSTY.getCode(),
                WateringCode.CHECK.getCode(),
                WateringCode.LEAVE_HER_ALONE.getCode(),
                WateringCode.LATE_WATERING.getCode()
        );

        IntStream.range(0, expectedWateringCodes.size())
                .forEach(i -> assertEquals(expectedWateringCodes.get(i), result.get(i).getGardenDetail().getWateringCode()));
    }

    @Test
    @DisplayName("전체 식물의 계산 값 반환 with 약품 정보")
    void getAll_WhenPlantExist_ShouldReturnAllPlantCalcualtedDtoAndChemicalInfo() {
        Long gardenerId = 1L;
        int missedDay = 5;

        List<PlantResponse> rawGardens = Arrays.asList(
                (PlantResponse) plantResponseFactory.getPostponedPlant(),
                (PlantResponse) plantResponseFactory.getPlantWithNoWateringRecord(),
                (PlantResponse) plantResponseFactory.getPlantWithOneWateringRecord(),
                (PlantResponse) plantResponseFactory.getWateredTodayPlant(),
                (PlantResponse) plantResponseFactory.getThirstyPlant(3),
                (PlantResponse) plantResponseFactory.getCheckingPlant(3),
                (PlantResponse) plantResponseFactory.getLeaveHerAlonePlant(3),
                (PlantResponse) plantResponseFactory.getDryOutPlant(3, missedDay)
        );


        List<ChemicalUsage> chemicalUsages = Arrays.asList(
                new ChemicalUsage(1L, 180, "코니도", LocalDate.now().minusDays(100)),
                new ChemicalUsage(2L, 30, "메네델", LocalDate.now().minusDays(30)), // valid one
                new ChemicalUsage(3L, 7, "하이포넥스", LocalDate.now().minusDays(10))
        );

        // plantId, gardenerId
        when(chemicalInfoService.getChemicalUsagesByPlantId(anyLong(), anyLong())).thenReturn(chemicalUsages);
        when(plantRepository.findAllPlantsWithLatestWateringDate(gardenerId)).thenReturn(rawGardens);

        // when
        List<GardenResponse> result = gardenService.getAll(gardenerId);

        // then
        assertNotNull(result);
        assertEquals(rawGardens.size(), result.size());

        // 물주기 코드 검증
        List<String> expectedWateringCodes = Arrays.asList(
                WateringCode.YOU_ARE_LAZY.getCode(),
                WateringCode.NOT_ENOUGH_RECORD.getCode(),
                WateringCode.NOT_ENOUGH_RECORD.getCode(),
                WateringCode.WATERED_TODAY.getCode(), // 약품 정보 필요
                WateringCode.THIRSTY.getCode(), // 약품 정보 필요
                WateringCode.CHECK.getCode(),
                WateringCode.LEAVE_HER_ALONE.getCode(),
                WateringCode.LATE_WATERING.getCode()
        );

        IntStream.range(0, expectedWateringCodes.size())
                .forEach(i -> assertEquals(expectedWateringCodes.get(i), result.get(i).getGardenDetail().getWateringCode()));

        // 약품 정보 검증
        for(GardenResponse gardenResponse : result) {
            String wateringCode = gardenResponse.getGardenDetail().getWateringCode();

            if(WateringCode.CHECK.getCode().equals(wateringCode) || WateringCode.THIRSTY.getCode().equals(wateringCode)){
                assertEquals(chemicalUsages.get(1).getChemicalId(), gardenResponse.getGardenDetail().getChemicalInfo().getChemicalId());
            }
        }
    }
}