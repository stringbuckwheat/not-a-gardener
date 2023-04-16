package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface PlantService {
    /* 하나의 식물 */
    PlantDto.Response getOnePlant(int plantNo);

    /* 식물 리스트 */
    List<PlantDto.Response> getPlantList(int memberNo);

    /* 식물 추가 */
    GardenDto.Response addPlant(PlantDto.Request plantRequest, Member member);

    /* 식물 수정 */
    GardenDto.Response modifyPlant(PlantDto.Request plantRequest, Member member);

    /* 식물 삭제 */
    void deletePlantByPlantNo(int plantNo);

    /* 식물들의 장소 정보 수정 */
    PlaceDto.Response modifyPlantPlace(PlaceDto.ModifyPlantPlace modifyPlantPlace);
}