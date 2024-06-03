package xyz.notagardener.status.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import xyz.notagardener.status.PlantStatus;

import java.util.Optional;

public interface PlantStatusRepository extends Repository<PlantStatus, Long>, PlantStatusRepositoryCustom {
    PlantStatus save(PlantStatus plantStatus);

    @EntityGraph(attributePaths = {"plant", "plant.gardener"})
    Optional<PlantStatus> findByPlantStatusId(Long plantStatusId);
    void delete(PlantStatus plantStatus);
}
