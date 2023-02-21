package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Fertilizer;
import com.buckwheat.garden.data.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FertilizerDto {
    private int fertilizerNo;
    private String fertilizerName;
    private int fertilizingPeriod;
    private int memberNo;

    public Fertilizer toEntityWithMember(Member member){
        return Fertilizer.builder()
                .fertilizerName(fertilizerName)
                .fertilizingPeriod(fertilizingPeriod)
                .member(member)
                .build();
    }
}
