package com.buckwheat.garden.data.dto.gardener;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Refresh {
    private Long gardenerId;
    private String refreshToken;
}
