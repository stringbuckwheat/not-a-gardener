package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.entity.Gardener;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GardenerDao extends Repository<Gardener, Long> {
    Gardener getReferenceById(Long gardenerId);

    Optional<Gardener> findById(Long gardenerId);

    Gardener save(Gardener gardener);

    void deleteById(Long gardenerId);
}
