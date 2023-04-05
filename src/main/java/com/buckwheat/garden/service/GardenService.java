package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface GardenService {
    /* 할 일이 있는 식물 리스트 반환 */
    GardenDto.GardenMain getGarden(int memberNo);

    /* 해당 유저의 전체 식물과 각종 계산값 반환 */
    List<GardenDto.GardenResponse> getPlantList(int memberNo);

    GardenDto.GardenResponse getGardenResponse(Plant plant, List<Chemical> chemicalList);

    GardenDto.WateringResponse addWateringInGarden(Member member, WateringDto.WateringRequest wateringRequest);
}