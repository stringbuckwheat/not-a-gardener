package com.buckwheat.garden.gardener;

import com.buckwheat.garden.gardener.gardener.GardenerDetail;

public interface GardenerRepositoryCustom {
    GardenerDetail findGardenerDetailByGardenerId(Long gardenerId);
}
