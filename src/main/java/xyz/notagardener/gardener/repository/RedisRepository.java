package xyz.notagardener.domain.gardener.repository;

import xyz.notagardener.domain.gardener.token.ActiveGardener;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RedisRepository extends Repository<ActiveGardener, Long> {
    Optional<ActiveGardener> findById(Long gardenerId);

    void deleteById(Long gardenerId);

    ActiveGardener save(ActiveGardener activeGardener);
}
