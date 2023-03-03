package com.buckwheat.garden.data.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PesticideDto {
    private int pesticideNo;
    private String pesticideName;
    private String pesticideType;
    private int pesticidePeriod;
}
