package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import lombok.Data;

@Data
public class PlaceDto {
    private int placeNo;
    private String placeName;
    private String artificialLight;
    private String outside;
    private int memberNo;

    public Place toEntityWithMember(Member member){
        return Place.builder()
                .placeName(placeName)
                .artificialLight(artificialLight)
                .outside(outside)
                .member(member)
                .build();
    }
}
