package com.buckwheat.garden.data.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlantStatusDto {
    private LocalDate updateDate;
    // private PlantStatusInfo plantStatusInfo;
    private int statusNo;
    private String statusName;

    private int plantNo;
    private int plantName;
}
