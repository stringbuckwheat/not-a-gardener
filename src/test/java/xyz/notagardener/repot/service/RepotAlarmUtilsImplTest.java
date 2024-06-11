package xyz.notagardener.repot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.dto.PlantStatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RepotAlarmUtilsImplTest {
    @InjectMocks
    private RepotAlarmUtilsImpl repotAlarmUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    static Stream<Integer> getMonthOfWinterScenario() {
        return Stream.of(11, 12, 1, 2, 3);
    }

    @ParameterizedTest
    @MethodSource("getMonthOfWinterScenario")
    @DisplayName("분갈이 알림: 11월 ~ 3월에는 알림을 보내지 않음")
    void isRepotNeeded_WhenTheseDaysTooCold_ShouldReturnFalse(int month) {
        // Mocking LocalDate.now()
        LocalDate date = LocalDate.of(2023, 11, 1);
        MockedStatic<LocalDate> mockedLocalDate = Mockito.mockStatic(LocalDate.class);
        mockedLocalDate.when(LocalDate::now).thenReturn(date);

        boolean result = repotAlarmUtils.isRepotNeeded(7, 2, null, LocalDate.now().minusDays(60));

        assertFalse(result);
        mockedLocalDate.close();
    }

    static Stream<Arguments> getNotEnoughRecordScenario() {
        return Stream.of(
                Arguments.of(10, 0),
                Arguments.of(0, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("getNotEnoughRecordScenario")
    @DisplayName("분갈이 알림: 데이터가 충분하지 않은 경우에도 보내지 않음")
    void isRepotNeeded_WhenNotEnoughRecord_ShouldReturnFalse(int recentPeriod, int earlyPeriod) {
        boolean result = repotAlarmUtils.isRepotNeeded(recentPeriod, earlyPeriod, null, LocalDate.now().minusDays(60));
        assertFalse(result);
    }

    @Test
    @DisplayName("분갈이 알림: 최근 분갈이 식물의 경우에도 보내지 않음")
    void isRepotNeeded_WhenJustRepotted_ShouldReturnFalse() {
        List<PlantStatusResponse> status = List.of(
                new PlantStatusResponse(1L, PlantStatusType.JUST_REPOTTED, YesOrNoType.Y, LocalDate.now().minusDays(3), LocalDateTime.now().minusMonths(1))
        );

        boolean result = repotAlarmUtils.isRepotNeeded(7, 2, status, LocalDate.now().minusDays(60));
        assertFalse(result);
    }

    @Test
    @DisplayName("분갈이 알림: 헤비 드링커가 1년 간 분갈이를 하지 않았을 시 알림 보냄")
    void isRepotNeeded_WhenHeavyDrinkerPastOneYear_ShouldReturnTrue() {
        List<PlantStatusResponse> status = List.of(
                new PlantStatusResponse(1L, PlantStatusType.HEAVY_DRINKER, YesOrNoType.Y, LocalDate.now().minusDays(3), LocalDateTime.now().minusMonths(1))
        );
        LocalDate createDate = LocalDate.now().minusMonths(13);

        boolean result = repotAlarmUtils.isRepotNeeded(2, 2, status, createDate);
        assertTrue(result);
    }

    @Test
    @DisplayName("분갈이 알림: 헤비 드링커 분갈이 1년 안 됐을 시 알림 보내지 않음")
    void isRepotNeeded_WhenHeavyDrinkerRepottedRecently_ShouldReturnFalse() {
        List<PlantStatusResponse> status = List.of(
                new PlantStatusResponse(1L, PlantStatusType.JUST_REPOTTED, YesOrNoType.Y, LocalDate.now().minusDays(3), LocalDateTime.now().minusMonths(1))
        );

        LocalDate createDate = LocalDate.now().minusMonths(2);

        boolean result = repotAlarmUtils.isRepotNeeded(1, 1, status, createDate);
        assertFalse(result);
    }

    static Stream<Arguments> getRepotCaculatingScenario() {
        // 최근 주기, 초기 주기, 기대값
        return Stream.of(
                Arguments.of(30, 10, true),
                Arguments.of(29, 9, true),
                Arguments.of(28, 9, true),
                Arguments.of(27, 9, true),
                Arguments.of(26, 8, true),
                Arguments.of(25, 8, true),
                Arguments.of(24, 8, true),
                Arguments.of(23, 7, true),
                Arguments.of(22, 7, true),

                Arguments.of(21, 10, true),
                Arguments.of(20, 10, true),
                Arguments.of(19, 9, true),
                Arguments.of(18, 9, true),
                Arguments.of(17, 8, true),
                Arguments.of(16, 8, true),
                Arguments.of(15, 7, true),
                Arguments.of(14, 7, true),
                Arguments.of(13, 6, true),
                Arguments.of(12, 6, true),
                Arguments.of(11, 5, true),
                Arguments.of(10, 5, true),
                Arguments.of(9, 4, true),
                Arguments.of(8, 4, true),
                Arguments.of(7, 3, true),
                Arguments.of(6, 3, true),
                Arguments.of(5, 2, true),
                Arguments.of(4, 2, true)
        );
    }

    @ParameterizedTest
    @MethodSource("getRepotCaculatingScenario")
    @DisplayName("분갈이 알림: 계산 로직 - 경계값, 분갈이 하는 조건")
    void isRepotNeeded_ShouldReturnCalculatedResult(int earlyPeriod, int recentPeriod, boolean expected) {
        boolean result = repotAlarmUtils.isRepotNeeded(recentPeriod, earlyPeriod, null, LocalDate.now().minusMonths(5));

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("getRepotCaculatingScenario")
    @DisplayName("분갈이 알림: 계산 로직 - 분갈이 안 하는 조건")
    void isRepotNeeded_WhenRepotNotNeeded_ShouldReturnCalculatedResult(int earlyPeriod, int recentPeriod, boolean expected) {
        boolean result = repotAlarmUtils.isRepotNeeded(recentPeriod + 1, earlyPeriod, null, LocalDate.now().minusMonths(5));

        assertEquals(!expected, result);
    }

    @ParameterizedTest
    @MethodSource("getRepotCaculatingScenario")
    @DisplayName("분갈이 알림: 계산 로직 - 분갈이 하는 조건")
    void isRepotNeeded_WhenRepotNeeded_ShouldReturnCalculatedResult(int earlyPeriod, int recentPeriod, boolean expected) {
        boolean result = repotAlarmUtils.isRepotNeeded(recentPeriod - 1, earlyPeriod, null, LocalDate.now().minusMonths(5));

        assertEquals(expected, result);
    }
}