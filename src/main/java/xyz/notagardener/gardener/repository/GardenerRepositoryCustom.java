package xyz.notagardener.gardener.repository;

import xyz.notagardener.gardener.dto.GardenerDetail;

import java.util.Optional;

public interface GardenerRepositoryCustom {
    Optional<GardenerDetail> findGardenerDetailByGardenerId(Long gardenerId);
}
