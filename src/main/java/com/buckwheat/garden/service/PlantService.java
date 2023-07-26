package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;

import java.util.List;

public interface PlantService {
    /* 식물 리스트 */
    List<PlantDto.Response> getAll(Long gardenerId);

    /* 하나의 식물 */
    PlantDto.Detail getDetail(Long plantId, Long gardenerId);

    /* 식물 추가 */
    GardenDto.Response add(Long gardenerId, PlantDto.Request plantRequest);

    /* 식물 수정 */
    GardenDto.Response modify(Long gardenerId, PlantDto.Request plantRequest);

    /* 식물들의 장소 정보 수정 */
    PlaceDto.Response modifyPlace(PlaceDto.ModifyPlace modifyPlantPlace, Long gardenerId);

    /* 식물 삭제 */
    void delete(Long plantId, Long gardenerId);
}