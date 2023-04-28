package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.ChemicalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Repository
public class ChemicalDaoImpl implements ChemicalDao {
    private final ChemicalRepository chemicalRepository;

    @Override
    public List<Chemical> getChemicalsByGardenerId(Long gardenerId){
        return chemicalRepository.findByGardener_GardenerId(gardenerId);
    }

    @Override
    public List<Watering> getWateringsByChemicalId(Long chemicalId){
        Chemical chemical = chemicalRepository.findByChemicalId(chemicalId).orElseThrow(NoSuchElementException::new);
        return chemical.getWaterings();
    }

    @Override
    public Chemical save(Chemical chemical) {
        return chemicalRepository.save(chemical);
    }

    @Override
    public void deleteByChemicalId(Long chemicalId) {
        chemicalRepository.deleteById(chemicalId);
    }
}
