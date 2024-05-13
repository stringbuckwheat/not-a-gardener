package xyz.notagardener.domain.chemical.repository;

import xyz.notagardener.domain.chemical.dto.ChemicalDto;
import xyz.notagardener.domain.watering.Watering;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ChemicalRepositoryCustom {
    List<ChemicalDto> findAllChemicals(Long gardenerId);

    Optional<ChemicalDto> findByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId);

    Long countWateringByChemicalId(Long chemicalId);

    List<Watering> findWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable);
}
