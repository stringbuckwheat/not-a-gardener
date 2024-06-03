package xyz.notagardener.watering.watering.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.watering.Watering;
import xyz.notagardener.watering.watering.AfterWateringCode;
import xyz.notagardener.watering.watering.dto.AfterWatering;
import xyz.notagardener.watering.watering.dto.WateringByDate;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.watering.repository.WateringRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("물주기(캘린더) 컴포넌트 테스트")
class WateringServiceImplTest {
    @Mock
    private WateringCommandService wateringCommandService;

    @Mock
    private WateringRepository wateringRepository;

    @InjectMocks
    private WateringServiceImpl wateringService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ArgumentsSource(WateringListProvider.class)
    @DisplayName("물주기(캘린더) 데이터 한달 + a 단위로 가져오기: 성공")
    void getAll(List<Watering> waterings, int expected) {
        // Given
        Long gardenerId = 5L;
        LocalDate date = LocalDate.of(2024, 5, 19);

        when(wateringRepository.findAllWateringListByGardenerId(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(waterings);

        // When
        Map<LocalDate, List<WateringByDate>> result = wateringService.getAll(gardenerId, date);

        // Then
        assertNotNull(result);
        assertEquals(expected, result.size());
    }

    @Test
    @DisplayName("물주기(캘린더) 기록 추가")
    void add() {
        // Given
        Long gardenerId = 1L;
        WateringRequest request = WateringRequest.builder().build();

        List<Watering> waterings = List.of(
                Watering.builder().wateringId(1L).wateringDate(LocalDate.now().minusDays(3)).build(),
                Watering.builder().wateringId(2L).wateringDate(LocalDate.now()).build() // 방금 추가한 물주기 정보
        );

        Plant plant = Plant.builder().plantId(1L).waterings(waterings).build();
        WateringMessage wateringMessage = new WateringMessage(AfterWateringCode.SECOND_WATERING.getCode(), 0);

        AfterWatering afterWatering = new AfterWatering(plant, wateringMessage);

        when(wateringCommandService.add(request, gardenerId)).thenReturn(afterWatering);

        // When
        WateringByDate result = wateringService.add(request, gardenerId);

        // Then
        assertNotNull(result);

        Watering lastWatering = waterings.get(waterings.size() - 1);

        assertEquals(lastWatering.getWateringId(), result.getId());
        assertEquals(lastWatering.getWateringDate(), result.getWateringDate());
        assertEquals(plant.getPlantId(), result.getPlantId());
    }
}