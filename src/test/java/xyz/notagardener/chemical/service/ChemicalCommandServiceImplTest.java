package xyz.notagardener.chemical.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.chemical.ChemicalType;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.gardener.GardenerRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

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
    @DisplayName("저장: Happy Path")
    void save_ShouldReturnSavedChemical() {
        // Given
        Long gardenerId = 1L;
        ChemicalDto chemicalDto = new ChemicalDto(null, "Chemical Name", ChemicalType.MICRONUTRIENT_FERTILIZER.getType(), 10);
        Gardener gardener = Gardener.builder().build();
        Chemical savedChemical = Chemical.builder()
                .chemicalId(1L) // 생성된 id 값
                .name(chemicalDto.getName())
                .type(chemicalDto.getType())
                .period(chemicalDto.getPeriod())
                .build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(chemicalRepository.save(any())).thenReturn(savedChemical);

        // When
        Chemical result = chemicalCommandService.save(gardenerId, chemicalDto, chemical -> chemical);

        // Then
        assertNotNull(result);
        assertEquals(savedChemical.getName(), result.getName());
        assertEquals(savedChemical.getType(), result.getType());
        assertEquals(savedChemical.getPeriod(), result.getPeriod());
    }

    @Test
    @DisplayName("저장: 회원 정보 없음 - 실패")
    void save_WhenGardenerNotExist_ThrowUsernameNotFoundException() {
        // Given
        Long gardenerId = 1L;
        ChemicalDto chemicalDto = new ChemicalDto(null, "Chemical Name", ChemicalType.MICRONUTRIENT_FERTILIZER.getType(), 10);

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(null);

        // When, Then
        Executable executable = () -> chemicalCommandService.save(gardenerId, chemicalDto, chemical -> chemical);
        UsernameNotFoundException e = assertThrows(UsernameNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_ACCOUNT.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("수정: Happy Path")
    void update_WhenChemicalExists_ShouldReturnUpdatedChemical() {
        // given
        Long gardenerId = 1L;
        Long chemicalId = 1L;
        ChemicalDto chemicalDto = new ChemicalDto(chemicalId, "Updated Name", "Updated Type", 10);
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Chemical existingChemical = Chemical.builder().gardener(gardener).build();

        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.of(existingChemical));

        // when
        Chemical result = chemicalCommandService.update(gardenerId, chemicalDto, chemical -> chemical);

        // then
        assertNotNull(result);
        assertEquals(chemicalDto.getName(), result.getName());
        assertEquals(chemicalDto.getType(), result.getType());
        assertEquals(chemicalDto.getPeriod(), result.getPeriod());
    }

    @Test
    @DisplayName("수정: 기존 약품이 존재하지 않는 경우 - 실패")
    void update_WhenChemicalNotExists_ShouldThrowNoSuchElementException() {
        // Given
        Long gardenerId = 1L;
        Long chemicalId = 1L;
        ChemicalDto chemicalDto = new ChemicalDto(chemicalId, "Updated Name", "Updated Type", 10);

        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.empty());

        // When, Then
        Executable execute = () -> chemicalCommandService.update(gardenerId, chemicalDto, chemical -> chemical);
        NoSuchElementException e = assertThrows(NoSuchElementException.class, execute);
        assertEquals(ExceptionCode.NO_SUCH_ITEM.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("수정: 기존 약품이 존재하지 않는 경우 - 실패")
    void update_WhenGardenerIdNotMatch_ThrowUnauthorizedAccessException() {
        // Given
        Long gardenerId = 1L; // 입력값
        Long ownerId = 2L; // 실제 약품 소유자
        Long chemicalId = 1L;
        ChemicalDto chemicalDto = new ChemicalDto(chemicalId, "Updated Name", "Updated Type", 10);

        Gardener owner = Gardener.builder().gardenerId(2L).build();
        Chemical existingChemical = Chemical.builder().gardener(owner).build();

        when(chemicalRepository.findById(chemicalId)).thenReturn(Optional.of(existingChemical));

        // When, Then
        Executable execute = () -> chemicalCommandService.update(gardenerId, chemicalDto, chemical -> chemical);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, execute);
        assertEquals(ExceptionCode.NOT_YOUR_THING.getCode(), e.getMessage());
    }
}