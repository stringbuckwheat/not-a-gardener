package xyz.notagardener.chemical.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import xyz.notagardener.chemical.Chemical;

import java.util.Optional;

public interface ChemicalRepository extends Repository<Chemical, Long>, ChemicalRepositoryCustom {
    Optional<Chemical> findById(Long id);

    @EntityGraph(attributePaths = {"gardener"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Chemical> findByChemicalId(Long chemicalId);

    Chemical save(Chemical chemical);
}
