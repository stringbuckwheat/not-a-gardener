package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.entity.Chemical;

import java.util.List;

public interface ChemicalDao {
    List<Chemical> getActivatedChemicalsByGardenerId(Long gardenerId);
    Chemical getChemicalByChemicalId(Long chemicalId);
    Chemical save(Long gardenerId, ChemicalDto.Request chemicalRequest);
    Chemical update(Long gardenerId, ChemicalDto.Request chemicalRequest);
    void deactivateChemicalByChemicalId(Long chemicalId);
}
