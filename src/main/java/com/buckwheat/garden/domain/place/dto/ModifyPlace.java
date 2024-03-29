package com.buckwheat.garden.domain.place.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ModifyPlace {
    private Long placeId;
    private List<Long> plants;
}
