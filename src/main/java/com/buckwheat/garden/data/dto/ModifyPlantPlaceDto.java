package com.buckwheat.garden.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ModifyPlantPlaceDto {
    int placeNo;
    List<Integer> plantList;
}
