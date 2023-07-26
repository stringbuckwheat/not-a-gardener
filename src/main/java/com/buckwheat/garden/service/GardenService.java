package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenDto;

import java.util.List;

public interface GardenService {
    /* 할 일이 있는 식물 리스트 반환 */
    GardenDto.GardenMain getGardenToDo(Long gardenerId);

    /* 해당 유저의 전체 식물과 각종 계산값 반환 */
    List<GardenDto.Response> getAll(Long gardenerId);
}