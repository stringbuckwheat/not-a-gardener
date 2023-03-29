package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface GardenService {
    /* 해당 유저의 전체 식물과 가장 최근 물/비료 준 날짜 리스트 반환 */
    List<GardenDto.GardenResponse> getGarden(int memberNo);
    GardenDto.GardenResponse getGardenResponse(Plant plant, List<Chemical> chemicalList);
}