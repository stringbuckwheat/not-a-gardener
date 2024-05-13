package xyz.notagardener.gardener.gardener;

import java.util.Optional;

public interface GardenerRepositoryCustom {
    Optional<GardenerDetail> findGardenerDetailByGardenerId(Long gardenerId);
}
