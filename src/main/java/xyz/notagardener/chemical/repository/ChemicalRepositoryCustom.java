package xyz.notagardener.chemical.repository;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.watering.Watering;

import java.util.List;
import java.util.Optional;

public interface ChemicalRepositoryCustom {
    List<ChemicalDto> findAllChemicals(Long gardenerId);

    Optional<ChemicalDto> findByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId);

    Long countWateringByChemicalId(Long chemicalId);

    List<Watering> findWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable);
}
