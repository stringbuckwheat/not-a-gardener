package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.repository.command.ChemicalCommandRepository;
import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.GardenerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Repository
public class ChemicalCommandRepositoryImpl implements ChemicalCommandRepository {
    private final ChemicalRepository chemicalRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    public ChemicalDto create(Long gardenerId, ChemicalDto chemicalRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        Chemical chemical = chemicalRepository.save(chemicalRequest.toEntity(gardener));
        return ChemicalDto.from(chemical);
    }

    @Override
    @Transactional
    public ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest) {
        Chemical chemical = chemicalRepository.findById(chemicalRequest.getId()).orElseThrow(NoSuchElementException::new);
        chemical.update(chemicalRequest.getName(), chemicalRequest.getType(), chemicalRequest.getPeriod());

        return ChemicalDto.from(chemical);
    }

    @Override
    @Transactional
    public void deactivate(Long chemicalId, Long gardenerId) {
        Chemical chemical = chemicalRepository.findByChemicalIdAndGardener_GardenerId(chemicalId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
        chemical.deactivate();
    }
}
