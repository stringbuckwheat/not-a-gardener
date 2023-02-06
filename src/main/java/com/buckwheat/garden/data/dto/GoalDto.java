package com.buckwheat.garden.data.dto;

import lombok.Data;

@Data
public class GoalDto {
    private String username;
    private int plantNo;
    private String goalTitle;
    private String goalContent;
}
