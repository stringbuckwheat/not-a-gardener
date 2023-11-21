package com.buckwheat.garden.domain.gardener.repository;

import com.buckwheat.garden.domain.gardener.dto.GardenerDetail;

public interface GardenerRepositoryCustom {
    GardenerDetail findGardenerDetailByGardenerId(Long gardenerId);
}
