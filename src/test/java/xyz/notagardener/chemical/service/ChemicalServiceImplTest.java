package xyz.notagardener.chemical.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.chemical.dto.ChemicalDetail;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.place.Place;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.watering.Watering;
import xyz.notagardener.watering.dto.WateringResponseInChemical;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChemicalServiceImplTest {

    @Mock
    private ChemicalRepository chemicalRepository;

    @InjectMocks
    private ChemicalServiceImpl chemicalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유효하지 않은 DTO")
    void test_WhenDtoInvalid_ShouldReturnInterpolatedMessage() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        // Given
        ChemicalDto invalidDto = new ChemicalDto(1L, "", "", -1);

        // When
        Set<ConstraintViolation<ChemicalDto>> violations = validator.validate(invalidDto);

        // Then
        assertEquals(3, violations.size());
    }

    @Test
    @DisplayName("읽기: 전체 약품 목록")
    void getAll_ShouldReturnListOfChemicalDto() {
        // Given
        Long gardenerId = 1L;
        List<ChemicalDto> expectedChemicals = new ArrayList<>();
        expectedChemicals.add(new ChemicalDto(null, "Chemical Name", "Chemical Type", 10));

        when(chemicalRepository.findAllChemicals(gardenerId)).thenReturn(expectedChemicals);

        // When
        List<ChemicalDto> result = chemicalService.getAll(gardenerId);

        // then
        assertEquals(expectedChemicals, result);
    }

    @Test
    @DisplayName("읽기: 한 약품의 상세 정보 - Happy Path")
    void getOne_ShouldReturnChemicalDetail() {
        // Given
        Long gardenerId = 1L;
        Long chemicalId = 1L;
        ChemicalDto chemicalDto = new ChemicalDto(chemicalId, "Chemical Name", "Chemical Type", 10);
        Long wateringSize = 5L;

        when(chemicalRepository.findByChemicalIdAndGardenerId(chemicalId, gardenerId)).thenReturn(Optional.of(chemicalDto));
        when(chemicalRepository.countWateringByChemicalId(chemicalId)).thenReturn(wateringSize);

        // When
        ChemicalDetail result = chemicalService.getOne(chemicalId, gardenerId);

        // Then
        assertEquals(chemicalDto, result.getChemical());
        assertEquals(wateringSize, result.getWateringSize());
    }

    // 한 약품 No Such Item
    @Test
    @DisplayName("읽기: 한 약품의 상세 정보 - 존재하지 않는 약품")
    void getOne_WhenChemicalNotExists_ShouldThrowNoSuchElementException() {
        // Given
        Long gardenerId = 1L;
        Long chemicalId = 1L;

        when(chemicalRepository.findByChemicalIdAndGardenerId(chemicalId, gardenerId)).thenReturn(Optional.empty());

        // When, Then
        Executable execute = () -> chemicalService.getOne(chemicalId, gardenerId);
        assertThrows(NoSuchElementException.class, execute);
    }

    @Test
    @DisplayName("읽기: 한 약품의 시비 기록")
    void getWateringsForChemical_ShouldReturnWateringResponseInChemicalList() {
        // Given
        Long chemicalId = 1L;
        Pageable pageable = mock(Pageable.class);

        Place place = Place.builder().build();
        Plant plant = Plant.builder().place(place).build();

        List<Watering> expectedWaterings = Arrays.asList(
                Watering.builder().wateringId(1L).plant(plant).build(),
                Watering.builder().wateringId(2L).plant(plant).build()
        );

        when(chemicalRepository.findWateringsByChemicalIdWithPage(chemicalId, pageable)).thenReturn(expectedWaterings);

        // When
        List<WateringResponseInChemical> result = chemicalService.getWateringsForChemical(chemicalId, pageable);

        // Then
        assertEquals(expectedWaterings.size(), result.size());
    }

    @Test
    @DisplayName("(논리) 삭제: Happy Path")
    void deactivate_WhenChemicalExists_ShouldDeactivate() {
        // Given
        Long chemicalId = 1L;
        Long gardenerId = 1L;

        Gardener owner = Gardener.builder().gardenerId(gardenerId).build();
        Chemical chemical = Chemical.builder().chemicalId(chemicalId).active("Y").gardener(owner).build();

        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.of(chemical));

        // When
        chemicalService.deactivate(chemicalId, gardenerId);

        // Then
        assertEquals("N", chemical.getActive());
    }

    @Test
    @DisplayName("(논리) 삭제: gardenerId 오류")
    void deactive_WhenNotThatGardener_ShouldThrowIllegalArgumentException() {
        // Given
        Long chemicalId = 1L;
        Long gardenerId = 1L; // 요청자의 gardenerId

        Gardener owner = Gardener.builder().gardenerId(2L).build();
        Chemical chemical = Chemical.builder().chemicalId(chemicalId).gardener(owner).build();

        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.of(chemical));

        // When, Then
        Executable execute = () -> chemicalService.deactivate(chemicalId, gardenerId);

        assertThrows(IllegalArgumentException.class, execute);
    }

    @Test
    @DisplayName("(논리) 삭제: chemicalId 오류")
    void deactive_WhenNotThatGardener_ShouldThrowNoSuchElementException() {
        // Given
        Long chemicalId = 1L;
        Long gardenerId = 1L;

        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.empty());

        // When, Then
        Executable execute = () -> chemicalService.deactivate(chemicalId, gardenerId);

        assertThrows(NoSuchElementException.class, execute);
    }
}