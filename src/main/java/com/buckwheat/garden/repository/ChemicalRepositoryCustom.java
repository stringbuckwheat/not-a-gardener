package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.dto.chemical.ChemicalDto;

import java.util.List;

public interface ChemicalRepositoryCustom {
    List<ChemicalDto> findAllChemicals(Long gardenerId);
    ChemicalDto findByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId);
}
