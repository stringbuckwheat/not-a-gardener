package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Watering;

import java.util.List;

public interface ChemicalDao {
    List<Chemical> getChemicalListByMemberNo(int memberNo);
    List<Watering> getWateringListByChemicalNo(int chemicalNo);
    Chemical save(Chemical chemical);
    void deleteByChemicalNo(int chemicalNo);
}
