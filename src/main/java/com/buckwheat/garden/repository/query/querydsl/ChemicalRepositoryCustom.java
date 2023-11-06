package com.buckwheat.garden.repository.query.querydsl;

import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.entity.Watering;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChemicalRepositoryCustom {
    List<ChemicalDto> findAllChemicals(Long gardenerId);
    ChemicalDto findByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId);
    Long countWateringByChemicalId(Long chemicalId);
    List<Watering> findWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable);
}
