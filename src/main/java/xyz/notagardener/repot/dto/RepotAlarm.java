package xyz.notagardener.repot.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RepotAlarm {
    private Long plantId;
    private String name;
    private Integer earlyWateringPeriod;
    private Integer recentWateringPeriod;
}
