package xyz.notagardener.gardener.gardener;

import org.springframework.data.repository.Repository;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.forgot.Username;

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
