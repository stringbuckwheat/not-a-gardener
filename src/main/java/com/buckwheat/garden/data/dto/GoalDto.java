package com.buckwheat.garden.data.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalDto {
    private int goalNo;
    private String goal;
    private String complete;
    private int plantNo;
    private String plantName;
}
