package xyz.notagardener.domain.gardener.repository;

import xyz.notagardener.domain.gardener.Gardener;
import xyz.notagardener.domain.gardener.dto.Username;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface GardenerRepository extends Repository<Gardener, Long>, GardenerRepositoryCustom {
    Gardener getReferenceById(Long gardenerId);

    Optional<Gardener> findById(Long gardenerId);
    Optional<Gardener> findByProviderIsNullAndUsername(String username);
    Optional<Gardener> findByUsernameAndProvider(String username, String provider);

    List<Username> findByProviderIsNullAndEmail(String email);

    Gardener save(Gardener gardener);

    void deleteById(Long gardenerId);
}
