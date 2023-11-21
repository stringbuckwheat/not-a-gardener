package com.buckwheat.garden.domain.gardener.repository;

import com.buckwheat.garden.domain.gardener.token.ActiveGardener;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RedisRepository extends Repository<ActiveGardener, Long> {
    Optional<ActiveGardener> findById(Long gardenerId);

    void deleteById(Long gardenerId);

    ActiveGardener save(ActiveGardener activeGardener);
}
