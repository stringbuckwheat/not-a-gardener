package xyz.notagardener.chemical.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.chemical.dto.ChemicalDetail;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.dto.WateringResponseInChemical;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;

import java.util.List;
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

    private Chemical getChemicalByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId) {
        Chemical chemical = chemicalRepository.findByChemicalId(chemicalId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_CHEMICAL));

        if(!gardenerId.equals(chemical.getGardener().getGardenerId())) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_CHEMICAL);
        }

        return chemical;
    }

    @Override
    @Transactional(readOnly = true)
    public ChemicalDetail getOne(Long chemicalId, Long gardenerId) {
        Chemical chemical = getChemicalByChemicalIdAndGardenerId(chemicalId, gardenerId);
        Long wateringSize = chemicalRepository.countWateringByChemicalId(chemicalId);

        return new ChemicalDetail(new ChemicalDto(chemical), wateringSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WateringResponseInChemical> getWateringsForChemical(Long chemicalId, Pageable pageable) {
        return chemicalRepository.findWateringsByChemicalIdWithPage(chemicalId, pageable)
                .stream()
                .map(WateringResponseInChemical::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChemicalDto add(Long gardenerId, ChemicalDto chemicalRequest) {
        Chemical chemical = chemicalCommandService.save(gardenerId, chemicalRequest);
        return new ChemicalDto(chemical);
    }

    @Override
    @Transactional
    public ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest) {
        Chemical chemical = chemicalCommandService.update(gardenerId, chemicalRequest);
        return new ChemicalDto(chemical);
    }

    @Override
    @Transactional
    public void deactivate(Long chemicalId, Long gardenerId) {
        Chemical chemical = chemicalRepository.findById(chemicalId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_CHEMICAL));
        Long ownerId = chemical.getGardener().getGardenerId(); // 실 소유자

        if (!ownerId.equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_CHEMICAL);
        }

        chemical.deactivate();
    }
}
