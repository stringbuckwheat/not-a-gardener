package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.projection.Username;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface GardenerRepository extends Repository<Gardener, Long> {
    Gardener getReferenceById(Long gardenerId);
    Optional<Gardener> findById(Long gardenerId);
    Optional<Gardener> findByProviderIsNullAndUsername(String username);
    Optional<Gardener> findByUsernameAndProvider(String username, String provider);
    List<Username> findByProviderIsNullAndEmail(String email);
    Gardener save(Gardener gardener);
    void deleteById(Long gardenerId);
}
