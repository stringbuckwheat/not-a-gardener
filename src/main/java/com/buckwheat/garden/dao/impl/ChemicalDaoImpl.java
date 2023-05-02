package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.GardenerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Repository
public class ChemicalDaoImpl implements ChemicalDao {
    private final ChemicalRepository chemicalRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    public List<Chemical> getActivatedChemicalsByGardenerId(Long gardenerId){
        return chemicalRepository.findByActiveAndGardener_GardenerId("Y", gardenerId);
    }

    @Override
    public Chemical getChemicalByChemicalId(Long chemicalId){
        return chemicalRepository.findByChemicalId(chemicalId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Chemical save(Long gardenerId, ChemicalDto.Request chemicalRequest) {
        Gardener gardener = gardenerRepository.findById(gardenerId).orElseThrow(NoSuchElementException::new);
        return chemicalRepository.save(chemicalRequest.toEntityWithGardener(gardener));
    }

    @Override
    public Chemical update(Long gardenerId, ChemicalDto.Request chemicalRequest) {
        Chemical prevChemical = chemicalRepository.findById(chemicalRequest.getId())
                .orElseThrow(NoSuchElementException::new);
        return chemicalRepository.save(prevChemical.update(chemicalRequest.getName(), chemicalRequest.getType(), chemicalRequest.getPeriod()));
    }

    @Override
    public void deactivateChemicalByChemicalId(Long chemicalId) {
        Chemical chemical = chemicalRepository.findByChemicalId(chemicalId).orElseThrow(NoSuchElementException::new);
        chemicalRepository.save(chemical.deactivate());
    }
}
