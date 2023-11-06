package com.buckwheat.garden.repository.command;

import com.buckwheat.garden.data.dto.chemical.ChemicalDto;

public interface ChemicalCommandRepository {
    ChemicalDto create(Long gardenerId, ChemicalDto chemicalRequest);

    ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest);

    void deactivate(Long chemicalId, Long gardenerId);
}
