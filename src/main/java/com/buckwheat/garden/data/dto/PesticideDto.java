package com.buckwheat.garden.data.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PesticideDto {
    private int pesticideNo;
    private String pesticideName;
    private int pesticidePeriod;
}
