package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.repository.querydsl.ChemicalRepositoryCustom;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ChemicalRepository extends Repository<Chemical, Long>, ChemicalRepositoryCustom {
    Optional<Chemical> findById(Long id);
    Optional<Chemical> findByChemicalIdAndGardener_GardenerId(Long chemicalId, Long gardenerId);
    Chemical save(Chemical chemical);
}
