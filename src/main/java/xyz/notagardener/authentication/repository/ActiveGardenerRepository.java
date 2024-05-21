package xyz.notagardener.authentication.repository;

import org.springframework.data.repository.Repository;
import xyz.notagardener.authentication.model.ActiveGardener;

import java.util.Optional;

public interface ActiveGardenerRepository extends Repository<ActiveGardener, Long> {
    Optional<ActiveGardener> findById(Long gardenerId);

    void deleteById(Long gardenerId);

    ActiveGardener save(ActiveGardener activeGardener);
}
