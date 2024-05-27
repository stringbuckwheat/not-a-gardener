package xyz.notagardener.gardener.repository;

import xyz.notagardener.gardener.dto.GardenerDetail;
import xyz.notagardener.gardener.dto.Username;

import java.util.List;
import java.util.Optional;

public interface GardenerRepositoryCustom {
    Optional<GardenerDetail> findGardenerDetailByGardenerId(Long gardenerId);
    List<Username> findByProviderIsNullAndEmail(String email);
}
