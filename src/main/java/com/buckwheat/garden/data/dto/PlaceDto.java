package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PlaceDto {
    private int placeNo;
    private String placeName;
    private String artificialLight;
    private String option;
    private int memberNo;

    private List<PlantDto> plantList;

    public Place toEntityWithMember(Member member){
        return Place.builder()
                .placeNo(placeNo)
                .placeName(placeName)
                .artificialLight(artificialLight)
                .option(option)
                .member(member)
                .build();
    }
}
