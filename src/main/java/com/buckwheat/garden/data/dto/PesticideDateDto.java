package com.buckwheat.garden.data.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PesticideDateDto {
    private int pesticideDateNo;
    private LocalDate pesticideDate;
    private int plantNo;
    private String plantName;
    private int pesticideNo;
    private String pesticideName;
}
