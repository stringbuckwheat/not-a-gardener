package com.buckwheat.garden.repository.command;

import com.buckwheat.garden.data.entity.Gardener;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GardenerCommandRepository extends Repository<Gardener, Long> {
    Optional<Gardener> findById(Long gardenerId);
    Optional<Gardener> findByProviderIsNullAndUsername(String username);
    Optional<Gardener> findByUsernameAndProvider(String username, String provider);
    Gardener save(Gardener gardener);

    void deleteById(Long gardenerId);
}
