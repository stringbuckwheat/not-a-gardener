package com.buckwheat.garden.data.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PesticideDateDto {
    private LocalDate pesticideDate;
    private int plantNo;
    private int pesticideInfoNo;
}
