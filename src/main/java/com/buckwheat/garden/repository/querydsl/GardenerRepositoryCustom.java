package com.buckwheat.garden.repository.querydsl;

import com.buckwheat.garden.data.dto.gardener.GardenerDetail;

public interface GardenerRepositoryCustom {
    GardenerDetail findGardenerDetailByGardenerId(Long gardenerId);
}
