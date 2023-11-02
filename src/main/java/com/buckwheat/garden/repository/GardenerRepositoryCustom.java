package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.dto.gardener.GardenerDetail;

public interface GardenerRepositoryCustom {
    GardenerDetail findGardenerDetailByGardenerId(Long gardenerId);
}
