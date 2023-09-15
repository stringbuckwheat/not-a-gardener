package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
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
    public Chemical getChemicalByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId){
        return chemicalRepository.findByChemicalIdAndGardener_GardenerId(chemicalId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Chemical save(Long gardenerId, ChemicalDto chemicalRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        return chemicalRepository.save(chemicalRequest.toEntity(gardener));
    }

    @Override
    public Chemical update(Long gardenerId, ChemicalDto chemicalRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        return chemicalRepository.save(chemicalRequest.toEntityForUpdate(gardener));
    }

    @Override
    public void deactivateChemical(Long chemicalId, Long gardenerId) {
        Chemical chemical = chemicalRepository.findByChemicalIdAndGardener_GardenerId(chemicalId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
        chemical.deactivate();

        chemicalRepository.save(chemical);
    }
}
