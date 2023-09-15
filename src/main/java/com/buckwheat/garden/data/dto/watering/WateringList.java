package com.buckwheat.garden.data.dto.watering;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class WateringList {
    private LocalDate wateringDate;
    List<WateringByDate> waterings;
}
