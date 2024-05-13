package xyz.notagardener.domain.gardener.repository;

import xyz.notagardener.domain.gardener.dto.GardenerDetail;

public interface GardenerRepositoryCustom {
    GardenerDetail findGardenerDetailByGardenerId(Long gardenerId);
}
