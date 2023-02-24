package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Fertilizer;
import com.buckwheat.garden.data.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FertilizerDto {
    private int fertilizerNo;
    private String fertilizerName;
    private String fertilizerType;
    private int fertilizingPeriod;
    private int memberNo;

    public Fertilizer toEntityWithMember(Member member){
        return Fertilizer.builder()
                .fertilizerName(fertilizerName)
                .fertilizingPeriod(fertilizingPeriod)
                .fertilizerType(fertilizerType)
                .member(member)
                .build();
    }
}
