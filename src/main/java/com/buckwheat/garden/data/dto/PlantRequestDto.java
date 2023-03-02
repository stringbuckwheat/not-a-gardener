package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PlantRequestDto {
    private int plantNo;
    private int placeNo;
    private String plantName;
    private String medium;
    private String plantSpecies;
    private int averageWateringPeriod;
    private int memberNo;

    private Member member;
}
