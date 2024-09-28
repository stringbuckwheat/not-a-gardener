package xyz.notagardener.chemical.repository;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.watering.model.Watering;

import java.util.List;

public interface ChemicalRepositoryCustom {
    List<ChemicalDto> findAllChemicals(Long gardenerId);

    Long countWateringByChemicalId(Long chemicalId);

    List<Watering> findWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable);
}
