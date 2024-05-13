package xyz.notagardener.domain.chemical.repository;

import xyz.notagardener.domain.chemical.Chemical;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ChemicalRepository extends Repository<Chemical, Long>, ChemicalRepositoryCustom {
    Optional<Chemical> findById(Long id);

    Optional<Chemical> findByChemicalIdAndGardener_GardenerId(Long chemicalId, Long gardenerId);

    Chemical save(Chemical chemical);
}
