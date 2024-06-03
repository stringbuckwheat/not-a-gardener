package xyz.notagardener.plant.garden.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.repot.service.RepotAlarmUtils;
import xyz.notagardener.watering.watering.dto.ChemicalUsage;
import xyz.notagardener.watering.watering.repository.WateringRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("식물 계산 컴포넌트 테스트")
class GardenResponseMapperImplTest {
    @Mock
    private WateringRepository wateringRepository;

    @Mock
    private ChemicalInfoServiceImpl chemicalInfoService;

    @Mock
    private RepotAlarmUtils repotAlarmUtils;

    @InjectMocks
    private GardenResponseMapperImpl gardenResponseMapper;

    private PlantResponseFactory plantResponseFactory = new PlantResponseFactory();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 오늘 물주기를 미룬 식물")
    void getGardenDetail_WhenPostponed_ShouldReturnCodeYouAreLazy() {
        // Given
        Long gardenerId = 1L;
        PlantResponse plantResponse = plantResponseFactory.getPostponedPlant();

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(WateringCode.YOU_ARE_LAZY.getCode(), result.getGardenDetail().getWateringCode());
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 물 준 기록 없음")
    void getGardenDetail_WhenNoWateringRecord_ShouldReturnCodeNoRecord() {
        // Given
        Long gardenerId = 1L;
        PlantResponse plantResponse = plantResponseFactory.getPlantWithNoWateringRecord();

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(WateringCode.NOT_ENOUGH_RECORD.getCode(), result.getGardenDetail().getWateringCode());
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 오늘 물 줌")
    void getGardenDetail_WhenWateredToday_ShouldReturnCodeWateredToday() {
        // Given
        Long gardenerId = 1L;
        PlantResponse plantResponse = plantResponseFactory.getWateredTodayPlant();

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();

        assertNotNull(result);
        assertEquals(WateringCode.WATERED_TODAY.getCode(), calculatedWateringCode);
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 물 주기 기록이 한 개인 경우")
    void getGardenDetail_WhenHasOneRecord_ShouldReturnCodeNotEnoughRecord() {
        // Given
        Long gardenerId = 1L;
        PlantResponse plantResponse = plantResponseFactory.getPlantWithOneWateringRecord();

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();

        assertNotNull(result);
        assertEquals(WateringCode.NOT_ENOUGH_RECORD.getCode(), calculatedWateringCode);
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 오늘 물 주는데, 비료는 안 줌")
    void getGardenDetail_WhenPeriodAndCalculatedValueSame_ShouldReturnCodeThirsty() {
        // Given
        Long gardenerId = 1L;
        int wateringPeriod = 3;
        LocalDate lastDrinkingDay = LocalDate.now().minusDays(wateringPeriod); // 3일 전 물 줌
        Long plantId = 2L;

        // 약품 정보
        List<ChemicalUsage> chemicalUsages = Arrays.asList(
                new ChemicalUsage(1L, 14, "메네델", LocalDate.now().minusDays(10)),
                new ChemicalUsage(2L, 7, "하이포넥스", lastDrinkingDay) // 마지막 관수일에 얘 줌
        );

        when(chemicalInfoService.getChemicalUsagesByPlantId(plantId, gardenerId)).thenReturn(chemicalUsages);

        PlantResponse plantResponse = plantResponseFactory.getThirstyPlant(wateringPeriod);

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();

        assertNotNull(result);
        assertEquals(WateringCode.THIRSTY.getCode(), calculatedWateringCode);
        assertNull(result.getGardenDetail().getChemicalInfo());
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 오늘 물 주는데, 비료도 줘야함")
    void getGardenDetail_WhenHasToWaterAndNeedChemicals_ShouldReturnCodeThirstyAndChemicalInfo() {
        // Given
        Long gardenerId = 1L;
        int wateringPeriod = 3;

        // 약품 정보
        // 줘야할 비료가 하나인 경우
        List<ChemicalUsage> chemicalUsages = Arrays.asList(
                new ChemicalUsage(1L, 14, "메네델", LocalDate.now().minusDays(11)),
                new ChemicalUsage(2L, 7, "하이포넥스", LocalDate.now().minusDays(10)) // 얘를 줘야함
        );

        when(chemicalInfoService.getChemicalUsagesByPlantId(anyLong(), anyLong())).thenReturn(chemicalUsages);

        PlantResponse plantResponse = plantResponseFactory.getThirstyPlant(wateringPeriod);

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();
        Long expectedChemicalId = chemicalUsages.get(1).getChemicalId();
        Long actualChemicalId = result.getGardenDetail().getChemicalInfo().getChemicalId();

        assertNotNull(result);
        assertEquals(WateringCode.THIRSTY.getCode(), calculatedWateringCode);
        assertEquals(expectedChemicalId, actualChemicalId);
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 차례가 된 여러 약품 중 순번 정하기")
    void getGardenDetail_WhenHasToWaterAndNeedChemicals_ShouldReturnCodeThirstyAndUrgentChemicalInfo() {
        // Given
        Long gardenerId = 1L;
        int wateringPeriod = 3;
        Long plantId = 2L;
        PlantResponse plantResponse = plantResponseFactory.getThirstyPlant(wateringPeriod);

        // 약품 정보
        List<ChemicalUsage> chemicalUsages = Arrays.asList(
                new ChemicalUsage(1L, 30, "메네델", LocalDate.now().minusDays(30)),
                new ChemicalUsage(2L, 7, "하이포넥스", LocalDate.now().minusDays(10))
        );

        when(chemicalInfoService.getChemicalUsagesByPlantId(anyLong(), anyLong())).thenReturn(chemicalUsages);

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();
        Long expectedChemicalId = chemicalUsages.get(0).getChemicalId();
        Long actualChemicalId = result.getGardenDetail().getChemicalInfo().getChemicalId();

        assertNotNull(result);
        assertEquals(WateringCode.THIRSTY.getCode(), calculatedWateringCode);
        assertEquals(expectedChemicalId, actualChemicalId);
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 약품 정보 없으면 맹물 반환")
    void getGardenDetail_WhenHasToWaterAndNoChemical_ShouldReturnCodeThirsty() {
        // Given
        Long gardenerId = 1L;
        int wateringPeriod = 3;
        Long plantId = 2L;

        // 약품 정보
        when(chemicalInfoService.getChemicalUsagesByPlantId(plantId, gardenerId)).thenReturn(new ArrayList<>());

        PlantResponse plantResponse = plantResponseFactory.getThirstyPlant(wateringPeriod);

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();

        assertNotNull(result);
        assertEquals(WateringCode.THIRSTY.getCode(), calculatedWateringCode);
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 물 주기 하루 전, 비료 X")
    void getGardenDetail_WhenDDayMinusOne_ShouldReturnCodeCheck() {
        // Given
        Long gardenerId = 1L;
        int wateringPeriod = 3;
        Long plantId = 2L;
        PlantResponse plantResponse = plantResponseFactory.getCheckingPlant(wateringPeriod);
        LocalDate lastDrinkingDay = plantResponse.getLatestWateringDate();

        // 약품 정보
        List<ChemicalUsage> chemicalUsages = Arrays.asList(
                new ChemicalUsage(1L, 14, "하이포넥스", LocalDate.now().minusDays(10)),
                new ChemicalUsage(2L, 7, "하이포넥스", lastDrinkingDay)
        );

        when(chemicalInfoService.getChemicalUsagesByPlantId(plantId, gardenerId)).thenReturn(chemicalUsages);


        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();

        assertNotNull(result);
        assertEquals(WateringCode.CHECK.getCode(), calculatedWateringCode);
        assertNull(result.getGardenDetail().getChemicalInfo());
    }

    @Test
    @DisplayName("식물 정보를 통해 계산된 DTO: 성공 - 물 주기 하루 전, 비료 O")
    void getGardenDetail_WhenDDayMinusOne_ShouldReturnCodeCheckWithChemicalInfo() {
        // Given
        Long gardenerId = 1L;
        int wateringPeriod = 3;
        Long plantId = 2L;
        PlantResponse plantResponse = plantResponseFactory.getCheckingPlant(wateringPeriod);
        LocalDate lastDrinkingDay = plantResponse.getLatestWateringDate();

        // 약품 정보
        // 줘야할 비료가 하나인 경우
        List<ChemicalUsage> chemicalUsages = Arrays.asList(
                new ChemicalUsage(1L, 14, "메네델", LocalDate.now().minusDays(11)),
                new ChemicalUsage(2L, 7, "하이포넥스", LocalDate.now().minusDays(10)) // 얘를 줘야함
        );

        when(chemicalInfoService.getChemicalUsagesByPlantId(anyLong(), anyLong())).thenReturn(chemicalUsages);


        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();
        Long expectedChemicalId = chemicalUsages.get(1).getChemicalId();

        Long actualChemicalId = result.getGardenDetail().getChemicalInfo().getChemicalId();

        assertNotNull(result);
        assertEquals(WateringCode.CHECK.getCode(), calculatedWateringCode);
        assertEquals(expectedChemicalId, actualChemicalId);
    }

    @Test
    @DisplayName("물주기까지 이틀 이상 남음, 놔두세요")
    void getGardenDetail_WhenDDayMoreThanTwo_ShouldReturnCodeLeaveHerAlone() {
        // Given
        Long gardenerId = 1L;
        int wateringPeriod = 10;
        PlantResponse plantResponse = plantResponseFactory.getLeaveHerAlonePlant(wateringPeriod);

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();

        assertNotNull(result);
        assertEquals(WateringCode.LEAVE_HER_ALONE.getCode(), calculatedWateringCode);
        assertNull(result.getGardenDetail().getChemicalInfo());
    }

    @Test
    @DisplayName("물주기 놓침, 며칠 늦었는지 알려줌")
    void getGardenDetail_WhenWateringDayMissed_ShouldReturnCodeNegativeAndMissedDay() {
        // Given
        Long gardenerId = 1L;
        int wateringPeriod = 5;
        int missedDay = 1; // 늦은 날

        PlantResponse plantResponse = plantResponseFactory.getDryOutPlant(wateringPeriod, missedDay);

        // When
        GardenResponse result = gardenResponseMapper.getGardenResponse(plantResponse, gardenerId);

        // Then
        String calculatedWateringCode = result.getGardenDetail().getWateringCode();

        assertNotNull(result);
        assertNull(result.getGardenDetail().getChemicalInfo());
        assertEquals(WateringCode.LATE_WATERING.getCode(), calculatedWateringCode);
        assertEquals(missedDay, result.getGardenDetail().getWateringDDay() * -1);
    }
}