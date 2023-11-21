package com.buckwheat.garden.domain.chemical.repository;

import com.buckwheat.garden.domain.chemical.dto.ChemicalDto;
import com.buckwheat.garden.domain.watering.Watering;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChemicalRepositoryCustom {
    List<ChemicalDto> findAllChemicals(Long gardenerId);

    ChemicalDto findByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId);

    Long countWateringByChemicalId(Long chemicalId);

    List<Watering> findWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable);
}
