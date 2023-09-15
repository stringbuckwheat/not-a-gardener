package com.buckwheat.garden.data.dto.gardener;

import com.buckwheat.garden.data.entity.Gardener;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleInfo {
    private Long gardenerId;
    private String name;
    private String provider;

    public static SimpleInfo from(Gardener gardener) {
        return new SimpleInfo(gardener.getGardenerId(), gardener.getName(), gardener.getProvider());
    }

    public static SimpleInfo from(Long gardenerId, String name, String provider){
        return new SimpleInfo(gardenerId, name, provider);
    }
}