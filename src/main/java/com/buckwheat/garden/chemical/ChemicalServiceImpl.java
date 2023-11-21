package com.buckwheat.garden.chemical;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.watering.dto.WateringResponseInChemical;
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
        ChemicalDto chemical = chemicalRepository.findByChemicalIdAndGardenerId(chemicalId, gardenerId);
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
        Chemical chemical = chemicalRepository.findByChemicalIdAndGardener_GardenerId(chemicalId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
        chemical.deactivate();
    }
}
