package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Chemical;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ChemicalRepository extends Repository<Chemical, Long>, ChemicalRepositoryCustom {
    Optional<Chemical> findById(Long id);
    List<Chemical> findByActiveAndGardener_GardenerId(String active, Long gardenerId);

    Optional<Chemical> findByChemicalIdAndGardener_GardenerId(Long chemicalId, Long gardenerId);
    Chemical save(Chemical chemical);
}
