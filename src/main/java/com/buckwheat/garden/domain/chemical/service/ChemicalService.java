package com.buckwheat.garden.domain.chemical.service;

import com.buckwheat.garden.domain.chemical.dto.ChemicalDetail;
import com.buckwheat.garden.domain.chemical.dto.ChemicalDto;
import com.buckwheat.garden.domain.watering.dto.WateringResponseInChemical;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChemicalService {
    List<ChemicalDto> getAll(Long gardenerId);

    ChemicalDetail getOne(Long chemicalId, Long gardenerId);

    List<WateringResponseInChemical> getWateringsForChemical(Long chemicalId, Pageable pageable);

    ChemicalDto add(Long gardenerId, ChemicalDto chemicalRequest);

    ChemicalDto update(Long gardenerId, ChemicalDto chemicalRequest);

    void deactivate(Long chemicalId, Long gardenerId);
}
