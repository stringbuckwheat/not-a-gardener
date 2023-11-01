package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Chemical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChemicalRepository extends JpaRepository<Chemical, Long> {
    List<Chemical> findByActiveAndGardener_GardenerId(String active, Long gardenerId);

    Optional<Chemical> findByChemicalIdAndGardener_GardenerId(Long chemicalId, Long gardenerId);
}
