package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.entity.Chemical;

import java.util.List;

public interface ChemicalDao {
    List<Chemical> getActivatedChemicalsByGardenerId(Long gardenerId);
    Chemical getChemicalByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId);
    Chemical save(Long gardenerId, ChemicalDto.Basic chemicalRequest);
    Chemical update(Long gardenerId, ChemicalDto.Basic chemicalRequest);
    void deactivateChemical(Long chemicalId, Long gardenerId);
}
