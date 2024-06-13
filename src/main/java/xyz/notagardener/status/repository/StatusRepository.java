package xyz.notagardener.status.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import xyz.notagardener.status.model.Status;

import java.util.Optional;

public interface StatusRepository extends Repository<Status, Long> {
    @EntityGraph(attributePaths = {"plant", "plant.gardener"})
    Optional<Status> findByStatusId(Long statusId);

    Optional<Status> findByPlant_PlantId(Long plantId);

    Status save(Status status);
}
