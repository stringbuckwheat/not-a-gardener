package xyz.notagardener.chemical.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.chemical.dto.ChemicalDetail;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.watering.dto.WateringResponseInChemical;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChemicalServiceImpl implements ChemicalService {
    private final ChemicalCommandService chemicalCommandService;
    private final ChemicalRepository chemicalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalDto> getAll(Long gardenerId) {
        return chemicalRepository.findAllChemicals(gardenerId);
    }

    @Override
    @Transactional(readOnly = true)
    public ChemicalDetail getOne(Long chemicalId, Long gardenerId) {
        ChemicalDto chemical = chemicalRepository.findByChemicalIdAndGardenerId(chemicalId, gardenerId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionCode.NO_SUCH_ITEM.getCode()));

        Long wateringSize = chemicalRepository.countWateringByChemicalId(chemicalId);

        return new ChemicalDetail(chemical, wateringSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WateringResponseInChemical> getWateringsForChemical(Long chemicalId, Pageable pageable) {
        return chemicalRepository.findWateringsByChemicalIdWithPage(chemicalId, pageable)
                .stream()
                .map(WateringResponseInChemical::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChemicalDto add(Long gardenerId, ChemicalDto chemicalRequest) {
        // 유효성 검사
        if (!chemicalRequest.isValidForSave()) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_REQUEST_DATA.getCode());
        }

        return chemicalCommandService.save(gardenerId, chemicalRequest, ChemicalDto::from);
    }

    @Override
    @Transactional
    public ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest) {
        // 유효성 검사
        if (!chemicalRequest.isValidForUpdate()) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_REQUEST_DATA.getCode());
        }

        return chemicalCommandService.update(gardenerId, chemicalRequest, ChemicalDto::from);
    }

    @Override
    @Transactional
    public void deactivate(Long chemicalId, Long gardenerId) {
        Chemical chemical = chemicalRepository.findById(chemicalId).orElseThrow(NoSuchElementException::new);
        Long ownerId = chemical.getGardener().getGardenerId(); // 실 소유자

        if (!ownerId.equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_THING.getCode());
        }

        chemical.deactivate();
    }
}
