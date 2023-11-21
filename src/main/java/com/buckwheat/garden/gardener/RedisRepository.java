package com.buckwheat.garden.gardener;

import com.buckwheat.garden.gardener.token.ActiveGardener;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RedisRepository extends Repository<ActiveGardener, Long> {
    Optional<ActiveGardener> findById(Long gardenerId);

    void deleteById(Long gardenerId);

    ActiveGardener save(ActiveGardener activeGardener);
}
