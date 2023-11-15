package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.repository.command.ChemicalCommandRepository;
import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.repository.dao.ChemicalDao;
import com.buckwheat.garden.repository.dao.GardenerDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Repository
public class ChemicalCommandRepositoryImpl implements ChemicalCommandRepository {
    private final ChemicalDao chemicalDao;
    private final GardenerDao gardenerDao;

    @Override
    public ChemicalDto create(Long gardenerId, ChemicalDto chemicalRequest) {
        Gardener gardener = gardenerDao.getReferenceById(gardenerId);
        Chemical chemical = chemicalDao.save(chemicalRequest.toEntity(gardener));
        return ChemicalDto.from(chemical);
    }

    @Override
    @Transactional
    public ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest) {
        Chemical chemical = chemicalDao.findById(chemicalRequest.getId()).orElseThrow(NoSuchElementException::new);
        chemical.update(chemicalRequest.getName(), chemicalRequest.getType(), chemicalRequest.getPeriod());

        return ChemicalDto.from(chemical);
    }

    @Override
    @Transactional
    public void deactivate(Long chemicalId, Long gardenerId) {
        Chemical chemical = chemicalDao.findByChemicalIdAndGardener_GardenerId(chemicalId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
        chemical.deactivate();
    }
}
