package xyz.notagardener.domain.chemical.service;

import xyz.notagardener.domain.chemical.Chemical;
import xyz.notagardener.domain.chemical.dto.ChemicalDetail;
import xyz.notagardener.domain.chemical.dto.ChemicalDto;
import xyz.notagardener.domain.chemical.repository.ChemicalRepository;
import xyz.notagardener.domain.watering.dto.WateringResponseInChemical;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(NoSuchElementException::new);

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
        return chemicalCommandService.save(gardenerId, chemicalRequest, ChemicalDto::from);
    }

    @Override
    @Transactional
    public ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest) {
        return chemicalCommandService.update(gardenerId, chemicalRequest, ChemicalDto::from);
    }

    @Override
    @Transactional
    public void deactivate(Long chemicalId, Long gardenerId) {
        Chemical chemical = chemicalRepository.findById(chemicalId).orElseThrow(NoSuchElementException::new);

        Long ownerId = chemical.getGardener().getGardenerId();

        if(!ownerId.equals(gardenerId)) {
            throw new IllegalArgumentException("B013");
        }

        chemical.deactivate();
    }
}
