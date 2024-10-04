package xyz.notagardener.gardener.repository;

import org.springframework.data.repository.Repository;
import xyz.notagardener.gardener.model.Gardener;

import java.util.Optional;

public interface GardenerRepository extends Repository<Gardener, Long>, GardenerRepositoryCustom {
    Gardener getReferenceById(Long gardenerId);

    Optional<Gardener> findById(Long gardenerId);
    Optional<Gardener> findByProviderIsNullAndUsername(String username);
    Optional<Gardener> findByUsernameAndProvider(String username, String provider);
    Optional<Gardener> findByEmailAndProvider(String email, String provider);

    Gardener save(Gardener gardener);

    void deleteById(Long gardenerId);
}
