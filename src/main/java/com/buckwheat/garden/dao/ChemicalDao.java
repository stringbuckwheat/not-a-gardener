package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Watering;

import java.util.List;

public interface ChemicalDao {
    List<Chemical> getChemicalsByGardenerId(Long gardenerId);
    List<Watering> getWateringsByChemicalId(Long chemicalId);
    Chemical save(Chemical chemical);
    void deleteByChemicalId(Long chemicalId);
}
