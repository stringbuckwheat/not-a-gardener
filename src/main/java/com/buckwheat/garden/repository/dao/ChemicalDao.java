package com.buckwheat.garden.repository.dao;

import com.buckwheat.garden.data.entity.Chemical;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ChemicalDao extends Repository<Chemical, Long> {
    Optional<Chemical> findById(Long id);

    Optional<Chemical> findByChemicalIdAndGardener_GardenerId(Long chemicalId, Long gardenerId);

    Chemical save(Chemical chemical);
}
