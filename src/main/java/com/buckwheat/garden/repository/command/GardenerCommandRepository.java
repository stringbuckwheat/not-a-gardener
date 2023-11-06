package com.buckwheat.garden.repository.command;

import com.buckwheat.garden.data.entity.Gardener;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GardenerCommandRepository extends Repository<Gardener, Long> {
    Optional<Gardener> findById(Long gardenerId);

    void deleteById(Long gardenerId);
}
