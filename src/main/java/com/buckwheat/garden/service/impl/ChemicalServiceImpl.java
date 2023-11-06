package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.repository.command.ChemicalCommandRepository;
import com.buckwheat.garden.data.dto.chemical.ChemicalDetail;
import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.dto.watering.WateringResponseInChemical;
import com.buckwheat.garden.repository.querydsl.ChemicalRepositoryCustom;
import com.buckwheat.garden.repository.WateringRepository;
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
    private final ChemicalRepositoryCustom chemicalRepository;
    private final ChemicalCommandRepository chemicalCommandRepository;
    private final WateringRepository wateringRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalDto> readAll(Long gardenerId) {
        return chemicalRepository.findAllChemicals(gardenerId);
    }

    @Override
    @Transactional(readOnly = true)
    public ChemicalDetail readOne(Long chemicalId, Long gardenerId) {
        ChemicalDto chemical = chemicalRepository.findByChemicalIdAndGardenerId(chemicalId, gardenerId);
        int wateringSize = wateringRepository.countByChemical_ChemicalId(chemicalId);

        return new ChemicalDetail(chemical, wateringSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WateringResponseInChemical> readWateringsForChemical(Long chemicalId, Pageable pageable) {
        return wateringRepository.findWateringsByChemicalIdWithPage(chemicalId, pageable)
                .stream()
                .map(WateringResponseInChemical::from)
                .collect(Collectors.toList());
    }

    @Override
    public ChemicalDto create(Long gardenerId, ChemicalDto chemicalRequest) {
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
