package xyz.notagardener.repot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.repot.Repot;
import xyz.notagardener.repot.repot.repository.RepotRepository;
import xyz.notagardener.repot.repot.service.RepotCommandService;
import xyz.notagardener.repot.repot.service.RepotServiceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@DisplayName("분갈이 컴포넌트 테스트")
class RepotServiceImplTest {
    @Mock
    private PlantRepository plantRepository;

    @Mock
    private RepotCommandService repotCommandService;

    @Mock
    private RepotRepository repotRepository;

    @InjectMocks
    private RepotServiceImpl repotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        when(repotCommandService.getRepotByRepotIdAndGardenerId(repotId, gardenerId)).thenReturn(repot);
        doNothing().when(repotRepository).delete(repot);

        // Then
        assertDoesNotThrow(() -> repotService.delete(repotId, gardenerId));
        verify(repotRepository, times(1)).delete(repot);
    }
}