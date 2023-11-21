package com.buckwheat.garden.domain.gardener.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Refresh {
    private Long gardenerId;
    private String refreshToken;
}
