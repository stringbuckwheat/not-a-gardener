package com.buckwheat.garden.gardener.gardener;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Refresh {
    private Long gardenerId;
    private String refreshToken;
}
