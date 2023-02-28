package com.buckwheat.garden.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter // InvalidDefinitionException: Serialize 하는 과정에서 접근 제한자가 public이거나 Getter/Setter를 이용하기 때문에 필드가 private로 선언되어있으면 JSON 변환 과정에서 문제가 발생
public class PlantDto {
    private int plantNo;
    private String plantName;
    private String plantSpecies;
    private int averageWateringPeriod;
    private int placeNo;
    private String placeName;
    private LocalDate createDate;
    private String medium;
}
