package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.chemical.ChemicalDetail;
import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.dto.watering.WateringResponseInChemical;
import com.buckwheat.garden.repository.command.ChemicalCommandRepository;
import com.buckwheat.garden.repository.query.querydsl.ChemicalRepositoryCustom;
import com.buckwheat.garden.service.ChemicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChemicalServiceImpl implements ChemicalService {
    private final ChemicalCommandRepository chemicalCommandRepository;
    private final ChemicalRepositoryCustom chemicalRepository; // Query Repository

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
    public ChemicalDto add(Long gardenerId, ChemicalDto chemicalRequest) {
        return chemicalCommandRepository.create(gardenerId, chemicalRequest);
    }

    @Override
    public ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest) {
        return chemicalCommandRepository.update(gardenerId, chemicalRequest);
    }

    @Override
    public void deactivate(Long chemicalId, Long gardenerId) {
        chemicalCommandRepository.deactivate(chemicalId, gardenerId);
    }
}
