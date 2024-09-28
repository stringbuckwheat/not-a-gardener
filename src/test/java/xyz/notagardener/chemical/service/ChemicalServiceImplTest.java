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
import xyz.notagardener.chemical.model.Chemical;
import xyz.notagardener.chemical.dto.ChemicalDetail;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.dto.WateringResponseInChemical;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.place.model.Place;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.watering.model.Watering;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("약품 컴포넌트 테스트")
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
        System.out.println(violations);
        assertEquals(4, violations.size());
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
    @DisplayName("읽기: 한 약품의 상세 정보 - 성공")
    void getOne_ShouldReturnChemicalDetail() {
        // Given
        Long gardenerId = 1L;
        Long chemicalId = 1L;
        Long wateringSize = 5L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Chemical chemical = Chemical.builder().chemicalId(chemicalId).gardener(gardener).build();

        when(chemicalRepository.findByChemicalId(chemicalId)).thenReturn(Optional.of(chemical));
        when(chemicalRepository.countWateringByChemicalId(chemicalId)).thenReturn(wateringSize);

        // When
        ChemicalDetail result = chemicalService.getOne(chemicalId, gardenerId);

        // Then
        assertEquals(chemicalId, result.getChemical().getId());
        assertEquals(wateringSize, result.getWateringSize());
    }

    // 한 약품 No Such Item
    @Test
    @DisplayName("읽기: 한 약품의 상세 정보 - 존재하지 않는 약품")
    void getOne_WhenChemicalNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long chemicalId = 1L;

        when(chemicalRepository.findByChemicalId(chemicalId)).thenReturn(Optional.empty());

        // When, Then
        Executable execute = () -> chemicalService.getOne(chemicalId, gardenerId);
        assertThrows(ResourceNotFoundException.class, execute);
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

//    static Stream<ChemicalDto> invalidChemicalSavingData() {
//        String validName = "하이포넥스";
//        String validType = ChemicalType.NPK_FERTILIZER.getType();
//        int validPeriod = 14;
//
//        return Stream.of(
//                ChemicalDto.builder().name(null).type(validType).period(validPeriod).build(),
//                ChemicalDto.builder().name("").type(validType).period(validPeriod).build(),
//                ChemicalDto.builder().name("reallyreallyreallyreallyreallyreallylongname").type(validType).period(validPeriod).build(),
//
//                ChemicalDto.builder().name(validName).type(null).period(validPeriod).build(),
//                ChemicalDto.builder().name(validName).type("이상한 타입").period(validPeriod).build(),
//
//                ChemicalDto.builder().name(validName).type(validType).period(0).build(),
//                ChemicalDto.builder().name(validName).type(validType).period(-1).build()
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("invalidChemicalSavingData")
//    @DisplayName("저장: 유효하지 않은 입력값 - 실패")
//    void save_WhenRequestDataInvalid_ThrowIllegalArgumentException(ChemicalDto chemicalDto) {
//        // Given
//        Long gardenerId = 1L;
//        Gardener gardener = Gardener.builder().build();
//
//        // When, Then
//        Executable executable = () -> chemicalService.add(gardenerId, chemicalDto);
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
//        assertEquals(ExceptionCode.INVALID_REQUEST_DATA.getCode(), e.getMessage());
//    }

//    static Stream<ChemicalDto> invalidChemicalUpdateData() {
//        Long validId = 1L;
//        String validName = "하이포넥스";
//        String validType = ChemicalType.NPK_FERTILIZER.getType();
//        int validPeriod = 14;
//
//        return Stream.of(
//                new ChemicalDto(null, validName, validType, validPeriod),
//                new ChemicalDto(-1L, validName, validType, validPeriod),
//
//                new ChemicalDto(validId, null, validType, validPeriod),
//                new ChemicalDto(validId, "", validType, validPeriod),
//                new ChemicalDto(validId, "reallyreallyreallyreallyreallyreallylongname", validType, validPeriod),
//
//                new ChemicalDto(validId, validName, null, validPeriod),
//                new ChemicalDto(validId, validName, "weird input", validPeriod),
//
//                new ChemicalDto(validId, validName, validType, -1),
//                new ChemicalDto(validId, validName, validType, 0)
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("invalidChemicalUpdateData")
//    @DisplayName("수정: 유효하지 않은 입력값 - 실패")
//    void update_WhenRequestDataInvalid_ThrowIllegalArgumentException(ChemicalDto chemicalDto) {
//        // Given
//        Long gardenerId = 1L;
//        Gardener gardener = Gardener.builder().build();
//
//        // When, Then
//        Executable executable = () -> chemicalService.update(gardenerId, chemicalDto);
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
//        assertEquals(ExceptionCode.INVALID_REQUEST_DATA.getCode(), e.getMessage());
//    }

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

        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, execute);
        assertEquals(ExceptionCode.NOT_YOUR_CHEMICAL, e.getCode());
    }

    @Test
    @DisplayName("(논리) 삭제: chemicalId 오류")
    void deactive_WhenNotThatGardener_ShouldThrowResourceNotFoundException() {
        // Given
        Long chemicalId = 1L;
        Long gardenerId = 1L;

        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.empty());

        // When, Then
        Executable execute = () -> chemicalService.deactivate(chemicalId, gardenerId);

        assertThrows(ResourceNotFoundException.class, execute);
    }
}