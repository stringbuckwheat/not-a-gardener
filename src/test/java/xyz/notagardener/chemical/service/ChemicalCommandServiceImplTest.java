package xyz.notagardener.chemical.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.chemical.ChemicalType;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.gardener.GardenerRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ChemicalCommandServiceImplTest {
    @Mock
    private GardenerRepository gardenerRepository;

    @Mock
    private ChemicalRepository chemicalRepository;

    @InjectMocks
    private ChemicalCommandServiceImpl chemicalCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("저장: 성공")
    void save_ShouldReturnSavedChemical() {
        // Given
        Long gardenerId = 1L;
        ChemicalDto chemicalDto = new ChemicalDto(null, "Chemical Name", ChemicalType.MICRONUTRIENT_FERTILIZER.getType(), 10);
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Chemical savedChemical = Chemical.builder()
                .chemicalId(1L) // 생성된 id 값
                .name(chemicalDto.getName())
                .type(chemicalDto.getType())
                .period(chemicalDto.getPeriod())
                .build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(chemicalRepository.save(any())).thenReturn(savedChemical);

        // When
        Chemical result = chemicalCommandService.save(gardenerId, chemicalDto);

        // Then
        assertNotNull(result);
        assertEquals(savedChemical.getName(), result.getName());
        assertEquals(savedChemical.getType(), result.getType());
        assertEquals(savedChemical.getPeriod(), result.getPeriod());
    }

    @Test
    @DisplayName("수정: 성공")
    void update_WhenChemicalExists_ShouldReturnUpdatedChemical() {
        // Given
        Long gardenerId = 1L;
        Long chemicalId = 1L;
        ChemicalDto chemicalDto = new ChemicalDto(chemicalId, "Updated Name", "Updated Type", 10);
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Chemical existingChemical = Chemical.builder().gardener(gardener).build();

        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.of(existingChemical));

        // When
        Chemical result = chemicalCommandService.update(gardenerId, chemicalDto);

        // Then
        assertNotNull(result);
        assertEquals(chemicalDto.getName(), result.getName());
        assertEquals(chemicalDto.getType(), result.getType());
        assertEquals(chemicalDto.getPeriod(), result.getPeriod());
    }

    static Stream<Arguments> provideUpdateFailureScenarios() {
        Gardener owner = Gardener.builder().gardenerId(2L).build();
        Chemical unauthorizedChemical = Chemical.builder().gardener(owner).build();

        return Stream.of(
                Arguments.of(Optional.empty(), ResourceNotFoundException.class),
                Arguments.of(Optional.of(unauthorizedChemical), UnauthorizedAccessException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUpdateFailureScenarios")
    @DisplayName("수정: 실패 시나리오")
    void update_WhenFailureScenarios_ShouldThrowException(Optional<Chemical> optionalChemical, Class<? extends Exception> expectedException) {
        // Given
        Long gardenerId = 1L;
        Long chemicalId = 1L;
        ChemicalDto chemicalDto = new ChemicalDto(chemicalId, "Updated Name", "Updated Type", 10);

        when(chemicalRepository.findById(chemicalId)).thenReturn(optionalChemical);

        // When, Then
        Executable execute = () -> chemicalCommandService.update(gardenerId, chemicalDto);
        assertThrows(expectedException, execute);
    }
}
