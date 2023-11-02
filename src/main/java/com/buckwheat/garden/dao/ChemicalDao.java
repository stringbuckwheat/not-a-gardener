package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.chemical.ChemicalDto;

import java.util.List;

public interface ChemicalDao {
    List<ChemicalDto> readAll(Long gardenerId);
    ChemicalDto readChemical(Long chemicalId, Long gardenerId);
    ChemicalDto create(Long gardenerId, ChemicalDto chemicalRequest);
    ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest);
    void deactivate(Long chemicalId, Long gardenerId);
}
