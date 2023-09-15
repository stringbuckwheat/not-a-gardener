package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.garden.GardenMain;
import com.buckwheat.garden.data.dto.garden.GardenResponse;

import java.util.List;

public interface GardenService {
    /* 할 일이 있는 식물 리스트 반환 */
    GardenMain getGardenToDo(Long gardenerId);

    /* 해당 유저의 전체 식물과 각종 계산값 반환 */
    List<GardenResponse> getAll(Long gardenerId);
}